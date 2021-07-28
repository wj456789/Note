package com.shzx.application.common.tool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class ToolFTPClient {
    public ToolFTPClient() {
    }

    public static boolean uploadFile(String ftpAddress, String port, String username, String password, String path, String filename, InputStream input) {
        boolean success = false;
        FTPClient ftp = new FTPClient();

        boolean var11;
        try {
            ftp.setControlEncoding("UTF-8");
            FTPClientConfig conf = new FTPClientConfig("WINDOWS");
            conf.setServerLanguageCode("zh");
            ftp.connect(ftpAddress, Integer.valueOf(port));
            ftp.login(username, password);
            ftp.setFileType(2);
            int reply = ftp.getReplyCode();
            if (FTPReply.isPositiveCompletion(reply)) {
                System.out.println("ftp服务器连接成功！");
                createDir(path, ftp);
                ftp.storeFile(new String(filename), input);
                input.close();
                ftp.logout();
                success = true;
                return success;
            }

            ftp.disconnect();
            var11 = success;
        } catch (IOException var22) {
            var22.printStackTrace();
            return success;
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException var21) {
                }
            }

        }

        return var11;
    }

    public static boolean createDir(String dir, FTPClient ftp) {
        try {
            String d = new String(dir.toString());
            if (ftp.changeWorkingDirectory(d)) {
                return true;
            } else {
                String[] arr = dir.split("/");
                StringBuffer sbfDir = new StringBuffer();
                String[] var5 = arr;
                int var6 = arr.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    String s = var5[var7];
                    sbfDir.append("/");
                    sbfDir.append(s);
                    d = new String(sbfDir.toString());
                    if (!ftp.changeWorkingDirectory(d) && !ftp.makeDirectory(d)) {
                        return false;
                    }
                }

                return ftp.changeWorkingDirectory(d);
            }
        } catch (Exception var9) {
            var9.printStackTrace();
            return false;
        }
    }

    public static InputStream downloadFile(String pathname, String filename, String ftpAddress, String port, String username, String password) {
        OutputStream os = null;
        FTPClient ftpClient = new FTPClient();
        InputStream inputStream = null;

        InputStream var11;
        try {
            System.out.println("开始下载文件");
            ftpClient.setControlEncoding("UTF-8");
            FTPClientConfig conf = new FTPClientConfig("WINDOWS");
            conf.setServerLanguageCode("zh");
            ftpClient.connect(ftpAddress, Integer.valueOf(port));
            ftpClient.login(username, password);
            ftpClient.setFileType(2);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
            }

            System.out.println("ftp服务器连接成功！");
            System.out.println(pathname);
            String[] path = pathname.split("\\/");
            System.out.println(path[1]);
            String pathName = "";

            for(int i = 1; i < path.length - 1; ++i) {
                pathName = pathName + "/" + path[i];
            }

            System.out.println(pathName);
            ftpClient.changeWorkingDirectory(pathName);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            FTPFile[] var14 = ftpFiles;
            int var15 = ftpFiles.length;

            for(int var16 = 0; var16 < var15; ++var16) {
                FTPFile file = var14[var16];
                if (filename.equalsIgnoreCase(file.getName())) {
                    inputStream = ftpClient.retrieveFileStream(file.getName());
                    System.out.println("下载文件成功");
                }
            }

            InputStream var34 = inputStream;
            return var34;
        } catch (Exception var30) {
            System.out.println("下载文件失败");
            var30.printStackTrace();
            var11 = inputStream;
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException var29) {
                    var29.printStackTrace();
                }
            }

            if (null != os) {
                try {
                    ((OutputStream)os).close();
                } catch (IOException var28) {
                    var28.printStackTrace();
                }
            }

        }

        return var11;
    }

    public static void initFtp(String ftpAddress, String port, String username, String password) {
        FTPClient ftp = new FTPClient();

        try {
            ftp.setControlEncoding("UTF-8");
            FTPClientConfig conf = new FTPClientConfig("WINDOWS");
            conf.setServerLanguageCode("zh");
            ftp.connect(ftpAddress, Integer.valueOf(port));
            ftp.login(username, password);
            ftp.setFileType(2);
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.out.println("ftp服务器连接失败！");
            }

            System.out.println("ftp服务器连接成功！");
        } catch (NumberFormatException var7) {
            var7.printStackTrace();
        } catch (SocketException var8) {
            var8.printStackTrace();
        } catch (IOException var9) {
            var9.printStackTrace();
        }

    }
}