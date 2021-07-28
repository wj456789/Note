/*    */ package com.shzx.application.common.tool;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ToolFileToZip {
    public ToolFileToZip() {
    }

    public static ByteArrayOutputStream fileToZip(List<String> fileList, String zipName, String tempFilePath) {
        byte[] buffer = new byte[1024];
        ZipOutputStream out = null;

        int dataLen;
        try {
            out = new ZipOutputStream(new FileOutputStream(tempFilePath + zipName + ".zip"));
            List<File> filedata = new ArrayList();
            int j = 0;

            int len;
            for(len = fileList.size(); j < len; ++j) {
                filedata.add(new File((String)fileList.get(j)));
            }

            j = 0;

            for(len = filedata.size(); j < len; ++j) {
                FileInputStream fis = new FileInputStream((File)filedata.get(j));
                out.putNextEntry(new ZipEntry(((File)filedata.get(j)).getName()));

                while((dataLen = fis.read(buffer)) > 0) {
                    out.write(buffer, 0, dataLen);
                }

                out.closeEntry();
                fis.close();
            }

            out.close();
        } catch (Exception var11) {
            var11.printStackTrace();
        }

        File filezip = new File(tempFilePath + zipName + ".zip");
        ByteArrayOutputStream baos = null;

        try {
            baos = new ByteArrayOutputStream();
            FileInputStream inStream = new FileInputStream(filezip);
            BufferedInputStream bis = new BufferedInputStream(inStream);

            for(dataLen = bis.read(); dataLen != -1; dataLen = bis.read()) {
                baos.write(dataLen);
            }

            bis.close();
            inStream.close();
        } catch (Exception var10) {
            var10.printStackTrace();
        }

        return baos;
    }
}