package com.shzx.application.common.tool;

import java.security.MessageDigest;

public class ToolMD5 {
    public static final String ENCODE = "UTF-8";

    public ToolMD5() {
    }

    public static String encryptPassword(String password, String salt) {
        StringBuffer builder = new StringBuffer();
        builder.append(password);
        builder.append(salt);
        return hash(builder.toString());
    }

    public static String encrypt(String content) {
        StringBuffer builder = new StringBuffer();
        builder.append(content);
        return hash(builder.toString());
    }

    public static String hash(String s) {
        return null != s && !s.equals("") ? hash(s, "UTF-8") : "";
    }

    public static String hash(String s, String charsetName) {
        try {
            return new String(toHex(md5(s)).getBytes(charsetName), charsetName);
        } catch (Exception var3) {
            throw new RuntimeException(var3.getMessage());
        }
    }

    private static byte[] md5(String s) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(s.getBytes("UTF-8"));
            return algorithm.digest();
        } catch (Exception var2) {
            throw new RuntimeException(var2.getMessage());
        }
    }

    private static final String toHex(byte[] hash) {
        if (null == hash) {
            return null;
        } else {
            StringBuffer buf = new StringBuffer(hash.length * 2);

            for(int i = 0; i < hash.length; ++i) {
                if ((hash[i] & 255) < 16) {
                    buf.append("0");
                }

                buf.append(Long.toString((long)(hash[i] & 255), 16));
            }

            return buf.toString();
        }
    }

    public static void main(String[] args) {
        System.out.println(encryptPassword("123456", "asdfwert1234"));
    }
}