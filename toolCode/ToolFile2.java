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

	private static final int BUFFER = 512;
	private static final int TOOBIG = 0x6400000; // max size of unzipped data, 100MB
	private static final int TOOMANY = 1024; // max number of files


    public static void decompressTar(String sourcePath, String destDir) {
        TarEntry entry;
		//接收byte[]数据  TarInputStream tis = new TarInputStream(new ByteArrayInputStream(sourceData))
		try (FileInputStream fis = new FileInputStream(sourcePath);
			 TarInputStream tis = new TarInputStream(fis)) {
			while ((entry = tis.getNextEntry()) != null) {
				int total = 0;
				int entries = 0;
				byte[] data = new byte[BUFFER];
				String destPath = FileUtil.sanitzeFileName(entry.getName(), destDir);
				int count;
				try (FileOutputStream fos = new FileOutputStream(destPath)) {
					while ((count = tis.read(data, 0, BUFFER)) != -1) {
						total += count;
						if (total > TOOBIG) {
							log.error("fileSize in package is too big");
						}
						fos.write(data, 0, count);
					}
					if (++entries > TOOMANY) {
						log.error("fileNum in package is too many");
					}
				}
			}
		} catch (IOException e) {
			log.error("decompressTar error", e);
		}
    }
	
	/**
	 * 文件路径检查
	 *
	 * @param entryName 文件名称
	 * @param intendedDir 文件路径
	 * @return 文件全路径
	 * @throws IOException
	 */
	public static String sanitzeFileName(String entryName, String intendedDir) throws IOException {
		String canonicalPath = new File(intendedDir, entryName).getCanonicalPath();
		String canonicalID = new File(intendedDir).getCanonicalPath();
		if (canonicalPath.startsWith(canonicalID)) {
			return canonicalPath;
		} else {
			throw new IllegalStateException("File is outside extraction target directory.");
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
