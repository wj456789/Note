package com.shzx.application.common.tool;

import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
@Slf4j
public class ToolProperties {
    public static final String param = "application-dev.properties";

    public ToolProperties() {
    }

    public static Properties getProperties(String propertieName) {
        try {
            Properties pro = new Properties();
            InputStream in = ToolProperties.class.getClassLoader().getResourceAsStream(propertieName);
            pro.load(in);
            in.close();
            return pro;
        } catch (FileNotFoundException var3) {
        } catch (IOException var4) {
        }

        return null;
    }

    public static String getValue(String key) {
        Properties properties = getProperties("application-dev.properties");
        return properties != null ? properties.getProperty(key) : null;
    }

    public static String getValue(String param, String key) {
        Properties properties = getProperties(param);
        return properties != null ? properties.getProperty(key) : null;
    }

    public static void setValue(String param,String key, String value){
        Properties properties = getProperties(param);
        properties.setProperty(key, value);
        FileOutputStream fileOutputStream = null;
        try {

            String path = ToolProperties.class.getClassLoader().getResources(param).nextElement().getPath();
            System.out.println("path:"+path);
            fileOutputStream = new FileOutputStream(path);
            properties.store(fileOutputStream, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fileOutputStream){
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                log.error("文件流关闭出现异常",e);
            }
        }
    }

    public static void main(String[] args) {
        String value = ToolProperties.getValue("config/application-log.properties","logging.file.path");
        System.out.println(value);

        ToolProperties.setValue("config/application-log.properties","logging.file.path","xwahi");
        System.out.println(ToolProperties.getValue("config/application-log.properties", "logging.file.path"));
    }
}