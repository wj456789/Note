package com.huawei.broker.hive.utils;

import com.huawei.broker.common.exception.BrokerException;
import com.huawei.broker.common.util.FIUtil;
import com.huawei.broker.common.util.HttpClientPoolUtil;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.springframework.http.HttpStatus;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class ToolFile2 {
    private static final int BUFFER = 512;
    private static final int TOOBIG = 0x6400000; // max size of unzipped data, 100MB
    private static final int TOOMANY = 1024; // max number of files

    public static String sanitzeFileName(String entryName, String intendedDir) throws IOException {
        String canonicalPath = new File(intendedDir, entryName).getCanonicalPath();
        String canonicalID = new File(intendedDir).getCanonicalPath();
        if (canonicalPath.startsWith(canonicalID)) {
            return canonicalPath;
        } else {
            throw new IllegalStateException("File is outside extraction target directory.");
        }
    }

    public static void decompressTar(String destTempDir, byte[] sourceData) {
        File dirFile = new File(destTempDir);
        FileUtil.deleteDirectory(dirFile);
        dirFile.mkdirs();
        TarEntry entry;
        TarInputStream tis = null;
        FileOutputStream fos = null;
        try {
            tis = new TarInputStream(new ByteArrayInputStream(sourceData));
            while ((entry = tis.getNextEntry()) != null) {
                int count;
                int total = 0;
                int entries = 0;
                byte[] data = new byte[BUFFER];
                String nameTemp = FileUtil.sanitzeFileName(entry.getName(), destTempDir);
                fos = new FileOutputStream(nameTemp);
                while ((count = tis.read(data, 0, BUFFER)) != -1) {
                    total += count;
                    if (total > TOOBIG) {
                        throw new BrokerException(
                                HttpStatus.INTERNAL_SERVER_ERROR.name(), "fileSize in package is too big");
                    }
                    fos.write(data, 0, count);
                }
                if (++entries > TOOMANY) {
                    throw new BrokerException(
                            HttpStatus.INTERNAL_SERVER_ERROR.name(), "fileNum in package is too many");
                }
            }
        } catch (IOException e) {
            log.error("decompressTar error", e);
            throw new BrokerException(HttpStatus.INTERNAL_SERVER_ERROR.name(), "decompressTar error");
        } finally {
            try {
                if (Objects.nonNull(fos)) {
                    fos.close();
                }
                if (Objects.nonNull(tis)) {
                    tis.close();
                }
            } catch (IOException e) {
                log.error("decompressTar error", e);
                throw new BrokerException(HttpStatus.INTERNAL_SERVER_ERROR.name(), "decompressTar error");
            }
        }
    }

    public static boolean deleteDirectory(File dir) {
        if (!dir.exists()) {
            return true;
        } else {
            File[] entries = dir.listFiles();
            for (int i = 0; i < entries.length; ++i) {
                if (entries[i].isDirectory()) {
                    if (!deleteDirectory(entries[i])) {
                        return false;
                    }
                } else if (!entries[i].delete()) {
                    return false;
                }
            }
            if (dir.delete()) {
                return true;
            } else {
                return false;
            }
        }
    }
}
