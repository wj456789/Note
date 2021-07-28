package com.shzx.application.common.tool;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.Cipher;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class ToolRasFile {
    static final String ALGORITHM = "RSA";
    static final int KEYSIZE = 1024;
    static KeyPairGenerator keyPairGen;
    static KeyPair keyPair;
    static RSAPrivateKey privateKey;
    static RSAPublicKey publicKey;

    public ToolRasFile() {
    }

    public String encryptFile(ToolRasFile encrypt, File file, File newFile) {
        try {
            if (!newFile.exists()) {
                newFile.getParentFile().mkdirs();
                newFile.createNewFile();
            } else {
                newFile.delete();
                newFile.createNewFile();
            }

            InputStream is = new FileInputStream(file);
            OutputStream os = new FileOutputStream(newFile);
            byte[] bytes = new byte[1];

            while(is.read(bytes) > 0) {
                byte[] e = encrypt.encrypt(publicKey, bytes);
                bytes = new byte[1];
                os.write(e, 0, e.length);
            }

            os.close();
            is.close();
            System.out.println("write success");
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return (new BASE64Encoder()).encode(privateKey.getEncoded());
    }

    public String decryptFile(ToolRasFile encrypt, File file, File newFile) {
        try {
            if (!newFile.exists()) {
                newFile.getParentFile().mkdirs();
                newFile.createNewFile();
            } else {
                newFile.delete();
                newFile.createNewFile();
            }

            InputStream is = new FileInputStream(file);
            OutputStream os = new FileOutputStream(newFile);
            byte[] bytes1 = new byte[64];

            while(is.read(bytes1) > 0) {
                byte[] de = encrypt.decrypt(privateKey, bytes1);
                bytes1 = new byte[64];
                os.write(de, 0, de.length);
            }

            os.close();
            is.close();
            System.out.println("write success");
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return (new BASE64Encoder()).encode(privateKey.getEncoded());
    }

    public String decryptFile(String privateKey, File file, File newFile) {
        try {
            if (!newFile.exists()) {
                newFile.getParentFile().mkdirs();
                newFile.createNewFile();
            } else {
                newFile.delete();
                newFile.createNewFile();
            }

            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec((new BASE64Decoder()).decodeBuffer(privateKey));
            keyf.generatePrivate(priPKCS8);
            RSAPrivateKey prikey = (RSAPrivateKey)keyf.generatePrivate(priPKCS8);
            InputStream is = new FileInputStream(file);
            OutputStream os = new FileOutputStream(newFile);
            byte[] bytes1 = new byte[64];

            while(is.read(bytes1) > 0) {
                byte[] de = this.decrypt(prikey, bytes1);
                bytes1 = new byte[64];
                os.write(de, 0, de.length);
            }

            os.close();
            is.close();
            System.out.println("write success");
        } catch (Exception var11) {
            var11.printStackTrace();
        }

        return (new BASE64Encoder()).encode(ToolRasFile.privateKey.getEncoded());
    }

    protected byte[] encrypt(RSAPublicKey publicKey, byte[] obj) {
        if (publicKey != null) {
            try {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(1, publicKey);
                return cipher.doFinal(obj);
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

        return null;
    }

    protected byte[] decrypt(RSAPrivateKey privateKey, byte[] obj) {
        if (privateKey != null) {
            try {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(2, privateKey);
                return cipher.doFinal(obj);
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

        return null;
    }

    public static void main(String[] args) {
        ToolRasFile encrypt = new ToolRasFile();
        File file = new File("D:\\excel\\test.txt");
        File newFile = new File("D:\\excel\\test1.txt");
        String key = encrypt.encryptFile(encrypt, file, newFile);
        System.out.println(key);
        File file1 = new File("D:\\excel\\test1.txt");
        File newFile1 = new File("D:\\excel\\test2.txt");
        String key2 = encrypt.decryptFile(key, file1, newFile1);
        System.out.println(key2);
    }

    static {
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(512);
            keyPair = keyPairGen.generateKeyPair();
            privateKey = (RSAPrivateKey)keyPair.getPrivate();
            publicKey = (RSAPublicKey)keyPair.getPublic();
        } catch (NoSuchAlgorithmException var1) {
            var1.printStackTrace();
        }

    }
}