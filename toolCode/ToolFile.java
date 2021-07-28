package com.shzx.application.common.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ToolFile {
    public static MessageDigest MD5 = null;

    public ToolFile() {
    }

    public static String fileMD5(File file) {
        FileInputStream fileInputStream = null;

        Object var3;
        try {
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];

            int length;
            while((length = fileInputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }

            String var4 = (new BigInteger(1, MD5.digest())).toString(16);
            return var4;
        } catch (IOException var14) {
            var14.printStackTrace();
            var3 = null;
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException var13) {
                var13.printStackTrace();
            }

        }

        return (String)var3;
    }

    public static boolean createDirectory(String dir) {
        File f = new File(dir);
        if (!f.exists()) {
            f.mkdirs();
            return true;
        } else {
            return false;
        }
    }

    public static void createNewFile(String fileDirectoryAndName, String fileContent) {
        try {
            File myFile = new File(fileDirectoryAndName);
            if (myFile.exists()) {
                throw new Exception("The new file already exists!");
            }

            myFile.createNewFile();
            write(fileContent, fileDirectoryAndName);
        } catch (Exception var3) {
            System.out.println("无法创建新文件！");
            var3.printStackTrace();
        }

    }

    public static boolean saveFileByPhysicalDir(String physicalPath, InputStream inputStream) {
        boolean flag = false;

        try {
            if (physicalPath.lastIndexOf("/") != -1) {
                File file = new File(physicalPath.substring(0, physicalPath.lastIndexOf("/")));
                if (!file.exists()) {
                    file.mkdirs();
                }
            }

            OutputStream os = new FileOutputStream(physicalPath);
            byte[] buffer = new byte[8192];

            int readBytes;
            while((readBytes = inputStream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, readBytes);
            }

            os.close();
            flag = true;
        } catch (FileNotFoundException var6) {
            var6.printStackTrace();
        } catch (IOException var7) {
            var7.printStackTrace();
        }

        return flag;
    }

    public static void saveAsFileOutputStream(String physicalPath, String content) {
        File file = new File(physicalPath);
        boolean b = file.getParentFile().isDirectory();
        if (!b) {
            File tem = new File(file.getParent());
            tem.mkdirs();
        }

        FileOutputStream foutput = null;

        try {
            foutput = new FileOutputStream(physicalPath);
            foutput.write(content.getBytes("UTF-8"));
        } catch (IOException var13) {
            var13.printStackTrace();
            throw new RuntimeException(var13);
        } finally {
            try {
                foutput.flush();
                foutput.close();
            } catch (IOException var12) {
                var12.printStackTrace();
                throw new RuntimeException(var12);
            }
        }

    }

    public static void write(String tivoliMsg, String logFileName) {
        try {
            byte[] bMsg = tivoliMsg.getBytes("UTF-8");
            FileOutputStream fOut = new FileOutputStream(logFileName, true);
            fOut.write(bMsg);
            fOut.close();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    public static String readTextFile(String realPath) throws Exception {
        File file = new File(realPath);
        if (!file.exists()) {
            System.out.println("File not exist!");
            return null;
        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(realPath), "UTF-8"));
            String temp = "";

            String txt;
            for(txt = ""; (temp = br.readLine()) != null; txt = txt + temp) {
            }

            br.close();
            return txt;
        }
    }

    public static void copyFile(String srcFile, String targetFile) throws IOException {
        if (checkExist(srcFile)) {
            FileInputStream fi = null;
            FileOutputStream fo = null;
            FileChannel in = null;
            FileChannel out = null;

            try {
                fi = new FileInputStream(srcFile);
                fo = new FileOutputStream(targetFile);
                in = fi.getChannel();
                out = fo.getChannel();
                in.transferTo(0L, in.size(), out);
            } catch (IOException var15) {
                var15.printStackTrace();
            } finally {
                try {
                    fi.close();
                    in.close();
                    fo.close();
                    out.close();
                } catch (IOException var14) {
                    var14.printStackTrace();
                }

            }
        }

    }

    public static boolean checkExist(String sFileName) {
        boolean result = false;

        try {
            File f = new File(sFileName);
            if (f.exists() && f.isFile()) {
                result = true;
            } else {
                result = false;
            }
        } catch (Exception var3) {
            result = false;
        }

        return result;
    }

    public static void copyDir(String sourceDir, String destDir) {
        File sourceFile = new File(sourceDir);
        if ((new File(destDir)).getParentFile().isDirectory()) {
            (new File(destDir)).mkdirs();
        }

        File[] files = sourceFile.listFiles();

        for(int i = 0; i < files.length; ++i) {
            String fileName = files[i].getName();
            String tempSource = sourceDir + "/" + fileName;
            String tempDest = destDir + "/" + fileName;
            if (files[i].isFile()) {
                try {
                    copyFile(tempSource, tempDest);
                } catch (IOException var9) {
                    var9.printStackTrace();
                }
            } else {
                copyDir(tempSource, tempDest);
            }
        }

        sourceFile = null;
    }

    public static void renameFile(String srcFile, String targetFile) throws IOException {
        try {
            copyFile(srcFile, targetFile);
            deleteFromName(srcFile);
        } catch (IOException var3) {
            throw var3;
        }
    }

    public static long getSize(String sFileName) {
        long lSize = 0L;

        try {
            File f = new File(sFileName);
            if (f.exists()) {
                if (f.isFile() && f.canRead()) {
                    lSize = f.length();
                } else {
                    lSize = -1L;
                }
            } else {
                lSize = 0L;
            }
        } catch (Exception var4) {
            lSize = -1L;
        }

        return lSize;
    }

    public static boolean deleteFromName(String sFileName) {
        boolean bReturn = true;

        try {
            File oFile = new File(sFileName);
            if (oFile.exists()) {
                boolean bResult = oFile.delete();
                if (!bResult) {
                    bReturn = false;
                }
            } else {
                bReturn = false;
            }
        } catch (Exception var4) {
            bReturn = false;
        }

        return bReturn;
    }

    public static boolean deleteDirectory(File dir) {
        if (!dir.exists()) {
            return false;
        } else {
            File[] entries = dir.listFiles();

            for(int i = 0; i < entries.length; ++i) {
                if (entries[i].isDirectory()) {
                    if (!deleteDirectory(entries[i])) {
                        return false;
                    }
                } else if (!entries[i].delete()) {
                    return false;
                }
            }

            if (!dir.delete()) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static int countLines(File file) {
        try {
            LineNumberReader rf = new LineNumberReader(new FileReader(file));
            Throwable var2 = null;

            int var5;
            try {
                long fileLength = file.length();
                rf.skip(fileLength);
                var5 = rf.getLineNumber();
            } catch (Throwable var15) {
                var2 = var15;
                throw var15;
            } finally {
                if (rf != null) {
                    if (var2 != null) {
                        try {
                            rf.close();
                        } catch (Throwable var14) {
                            var2.addSuppressed(var14);
                        }
                    } else {
                        rf.close();
                    }
                }

            }

            return var5;
        } catch (IOException var17) {
            var17.printStackTrace();
            return 0;
        }
    }

    public static List<String> lines(File file) {
        ArrayList list = new ArrayList();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            Throwable var3 = null;

            try {
                String line;
                try {
                    while((line = reader.readLine()) != null) {
                        list.add(line);
                    }
                } catch (Throwable var13) {
                    var3 = var13;
                    throw var13;
                }
            } finally {
                if (reader != null) {
                    if (var3 != null) {
                        try {
                            reader.close();
                        } catch (Throwable var12) {
                            var3.addSuppressed(var12);
                        }
                    } else {
                        reader.close();
                    }
                }

            }
        } catch (IOException var15) {
            var15.printStackTrace();
        }

        return list;
    }

    public static void main(String[] args) {
    }

    public static String getUrlPath(String url) {
        StringBuffer stringBuilder = new StringBuffer();
        Calendar cal = Calendar.getInstance();
        stringBuilder.append(url).append(cal.get(1)).append("/").append(cal.get(2) + 1).append("/").append(cal.get(5)).append("/").append(cal.get(10)).append("/").append(cal.get(12)).append("/").append(cal.get(13)).append("/").append((int)(Math.random() * 99999.0D)).append("/");
        return stringBuilder.toString();
    }

    public static Long calculateSize(int size) {
        int GB = 1073741824;
        int MB = 1048576;
        int KB = 1024;
        Long resultSize;
        if (size / GB >= 1) {
            resultSize = (long)(size / 1024);
        } else if (size / MB >= 1) {
            resultSize = (long)(size / 1024);
        } else if (size / KB >= 1) {
            resultSize = (long)(size / 1024);
        } else {
            resultSize = (long)(size / 1024);
        }

        return resultSize;
    }

    static {
        try {
            MD5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var1) {
            var1.printStackTrace();
        }

    }
}