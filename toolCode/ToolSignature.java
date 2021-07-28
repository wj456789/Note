package com.shzx.application.common.tool;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.tomcat.util.codec.binary.Base64;

public class ToolSignature {
    private static final String MAC_HMAC_SHA1 = "HmacSHA1";
    private static final String ENCODING_UTF8 = "UTF-8";

    public ToolSignature() {
    }

    public static String decodeBase64(String data) {
        return null != data && !data.equals("") ? decodeBase64(data, "UTF-8") : "";
    }

    public static String decodeBase64(String data, String charsetName) {
        try {
            return new String(Base64.decodeBase64(data.getBytes(charsetName)));
        } catch (UnsupportedEncodingException var3) {
            ToolLog.error("Base64 解码，指定编码格式", var3);
            return "";
        }
    }

    public static String encodeBase64(String data) {
        return null != data && !data.equals("") ? encodeBase64(data, "UTF-8") : "";
    }

    public static String encodeBase64(String data, String charsetName) {
        try {
            return new String(Base64.encodeBase64(data.getBytes(charsetName)));
        } catch (UnsupportedEncodingException var3) {
            ToolLog.error("encodeBase64 编码，指定编码格式", var3);
            return "";
        }
    }

    public static String encryptionHamcSha1(String data, String key) {
        return encryptionHamcSha1(data, key, "UTF-8");
    }

    public static String encryptionHamcSha1(String data, String key, String encoding) {
        if (null != data && !data.equals("") && null != key && !key.equals("")) {
            try {
                return encryptionHamcSha1(data.getBytes(encoding), key.getBytes(encoding));
            } catch (UnsupportedEncodingException var4) {
                ToolLog.error("按照指定编码格式进行 HmacSha1 加密。", var4);
                return "";
            }
        } else {
            return "";
        }
    }

    public static String encryptionHamcSha1(byte[] data, byte[] key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key, "HmacSHA1"));
            return byte2hex(mac.doFinal(data));
        } catch (NoSuchAlgorithmException var3) {
            ToolLog.error("HmacSha1加密", var3);
            return null;
        } catch (InvalidKeyException var4) {
            ToolLog.error("HmacSha1加密", var4);
            return null;
        }
    }

    public static String generator(String[] params) {
        return generator(params, "SHA-1");
    }

    public static String generator(String[] params, String algorithm) {
        Arrays.sort(params);
        StringBuffer content = new StringBuffer();
        String[] var4 = params;
        int var5 = params.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String param = var4[var6];
            content.append(param);
        }

        var4 = null;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            return byteToStr(messageDigest.digest(content.toString().getBytes()));
        } catch (NoSuchAlgorithmException var8) {
            ToolLog.error("生成token", var8);
            return "";
        }
    }

    public static String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer();

        for(int n = 0; b != null && n < b.length; ++n) {
            String stmp = Integer.toHexString(b[n] & 255);
            if (stmp.length() == 1) {
                hs.append('0');
            }

            hs.append(stmp);
        }

        return hs.toString();
    }

    private static String byteToStr(byte[] byteArray) {
        StringBuffer digest = new StringBuffer();

        for(int i = 0; i < byteArray.length; ++i) {
            digest.append(byteToHexStr(byteArray[i]));
        }

        return digest.toString();
    }

    private static String byteToHexStr(byte mByte) {
        char[] Digit = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[]{Digit[mByte >>> 4 & 15], Digit[mByte & 15]};
        return new String(tempArr);
    }
}