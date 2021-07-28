package com.shzx.application.common.tool;

import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToolHttp {
    private static Logger logger = LoggerFactory.getLogger(ToolHttp.class);
    private static int CONNECT_TIMEOUT = 6000;
    private static int READ_TIMEOUT = 6000;
    private static ThreadLocal<RequestConfig> commReqCfg = new ThreadLocal();

    ToolHttp() {
        commReqCfg.set(RequestConfig.custom().setConnectionRequestTimeout(CONNECT_TIMEOUT).setConnectTimeout(READ_TIMEOUT).setSocketTimeout(CONNECT_TIMEOUT).build());
    }

    public static void main(String[] args) throws Exception {
        System.out.println(get("http://www.baidu.com"));
        System.out.println(post("http://www.baidu.com"));
    }

    ToolHttp(int CONNECT_TIMEOUT, int READ_TIMEOUT) {
        commReqCfg.set(RequestConfig.custom().setConnectionRequestTimeout(CONNECT_TIMEOUT).setConnectTimeout(READ_TIMEOUT).setSocketTimeout(CONNECT_TIMEOUT).build());
    }

    private static InputStream requestPost(CloseableHttpClient client, String url, String data) throws Exception {
        HttpPost post = new HttpPost(url);
        post.setConfig((RequestConfig)commReqCfg.get());
        if (null != data) {
            HttpEntity entity = new StringEntity(data, "utf-8");
            post.setEntity(entity);
        }

        CloseableHttpResponse response = client.execute(post);
        return response.getEntity().getContent();
    }

    private static InputStream requestPut(CloseableHttpClient client, String url, JSONObject data, JSONObject header) throws Exception {
        HttpPut put = new HttpPut(url);
        put.setConfig((RequestConfig)commReqCfg.get());
        if (null != data) {
            HttpEntity entity = new StringEntity(data.toString(), "utf-8");
            put.setEntity(entity);
        }

        if (null != header) {
            Iterator it = header.keySet().iterator();

            while(it.hasNext()) {
                String key = (String)it.next();
                String value = header.getString(key);
                put.setHeader(key, value);
            }
        }

        CloseableHttpResponse response = client.execute(put);
        return response.getEntity().getContent();
    }

    private static InputStream requestDelete(CloseableHttpClient client, String url, JSONObject header) throws Exception {
        HttpDelete delete = new HttpDelete(url);
        delete.setConfig((RequestConfig)commReqCfg.get());
        if (null != header) {
            Iterator it = header.keySet().iterator();

            while(it.hasNext()) {
                String key = (String)it.next();
                String value = header.getString(key);
                delete.setHeader(key, value);
            }
        }

        CloseableHttpResponse response = client.execute(delete);
        return response.getEntity().getContent();
    }

    private static InputStream requestPost(CloseableHttpClient client, String url, JSONObject data, JSONObject header) throws Exception {
        HttpPost post = new HttpPost(url);
        post.setConfig((RequestConfig)commReqCfg.get());
        if (null != data) {
            HttpEntity entity = new StringEntity(data.toString(), "utf-8");
            post.setEntity(entity);
        }

        if (null != header) {
            Iterator it = header.keySet().iterator();

            while(it.hasNext()) {
                String key = (String)it.next();
                String value = header.getString(key);
                post.setHeader(key, value);
            }
        }

        CloseableHttpResponse response = client.execute(post);
        return response.getEntity().getContent();
    }

    private static InputStream requestGet(CloseableHttpClient client, String url, Map<String, Object> paramMap) throws Exception {
        URIBuilder uriB = new URIBuilder(url);
        if (paramMap != null && !paramMap.isEmpty()) {
            Iterator ite = paramMap.keySet().iterator();

            while(ite.hasNext()) {
                String key = (String)ite.next();
                uriB.setParameter(key, String.valueOf(paramMap.get(key)));
            }
        }

        HttpGet get = new HttpGet(uriB.build());
        get.setConfig((RequestConfig)commReqCfg.get());
        CloseableHttpResponse response = client.execute(get);
        return response.getEntity().getContent();
    }

    public static String get(String url, Map<String, Object> paramMap) throws Exception {
        logger.info("Http发送Get请求，Url：" + url + "; 参数：" + (new JSONObject(paramMap)).toString());
        CloseableHttpClient client = HttpClientBuilder.create().build();
        InputStream is = null;
        StringBuffer strB = new StringBuffer();

        try {
            is = requestGet(client, url, paramMap);
            byte[] bts = new byte[1024];
            boolean var6 = true;

            while(true) {
                int len;
                if ((len = is.read(bts)) == -1) {
                    is.close();
                    client.close();
                    break;
                }

                strB.append(new String(bts, 0, len, "utf-8"));
            }
        } catch (Exception var17) {
            logger.error("Http发送Get请求错误，Url：" + url + "; 参数：" + (new JSONObject(paramMap)).toString(), var17);
            throw var17;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception var16) {
                    logger.warn("Http发送Get关闭文件流错误");
                }
            }

            try {
                client.close();
            } catch (Exception var15) {
                logger.warn("Http发送Get关闭连接错误");
            }

        }

        logger.info("Http发送Get请求，Url：" + url + "; 参数：" + (new JSONObject(paramMap)).toString() + "; 结果：" + strB.toString());
        return strB.toString();
    }

    public static String get(Map<String, Object> paramMap, String url) throws Exception {
        logger.info("Http发送Get请求，Url：" + url + "; 头信息：" + (new JSONObject(paramMap)).toString());
        CloseableHttpClient client = HttpClientBuilder.create().build();
        StringBuffer strB = new StringBuffer();

        try {
            HttpGet httpGet = new HttpGet(url);
            if (null != paramMap) {
                Iterator var5 = paramMap.keySet().iterator();

                while(var5.hasNext()) {
                    String key = (String)var5.next();
                    BasicHeader h = new BasicHeader(key, paramMap.get(key) + "");
                    httpGet.setHeader(h);
                }
            }

            CloseableHttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            strB.append(EntityUtils.toString(entity, "utf-8"));
            response.close();
            client.close();
        } catch (Exception var15) {
            logger.error("Http发送Get请求错误，Url：" + url + "; 参数：" + (new JSONObject(paramMap)).toString(), var15);
            throw var15;
        } finally {
            try {
                client.close();
            } catch (Exception var14) {
                logger.warn("Http发送Get关闭连接错误");
            }

        }

        logger.info("Http发送Get请求，Url：" + url + "; 参数：" + (new JSONObject(paramMap)).toString() + "; 结果：" + strB.toString());
        return strB.toString();
    }

    public static String get(String url) throws Exception {
        logger.info("Http发送Get请求，Url：" + url);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        InputStream is = null;
        StringBuffer strB = new StringBuffer();

        try {
            is = requestGet(client, url, (Map)null);
            byte[] bts = new byte[1024];
            boolean var5 = true;

            while(true) {
                int len;
                if ((len = is.read(bts)) == -1) {
                    is.close();
                    client.close();
                    break;
                }

                strB.append(new String(bts, 0, len, "utf-8"));
            }
        } catch (Exception var16) {
            logger.error("Http发送Get请求错误，Url：" + url, var16);
            throw var16;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception var15) {
                    logger.warn("Http发送Get关闭文件流错误");
                }
            }

            try {
                client.close();
            } catch (Exception var14) {
                logger.warn("Http发送Get关闭连接错误");
            }

        }

        logger.info("Http发送Get请求，Url：" + url + "; 结果：" + strB.toString());
        return strB.toString();
    }

    public static String put(String url, JSONObject data, JSONObject header) throws Exception {
        logger.info("Http发送put请求，Url：" + url + "; 参数：" + data);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        InputStream is = null;
        StringBuffer strB = new StringBuffer();

        try {
            is = requestPut(client, url, data, header);
            byte[] bts = new byte[1024];
            boolean var7 = true;

            while(true) {
                int len;
                if ((len = is.read(bts)) == -1) {
                    is.close();
                    client.close();
                    break;
                }

                strB.append(new String(bts, 0, len, "utf-8"));
            }
        } catch (Exception var15) {
            logger.error("Http发送put请求错误，Url：" + url + "; 参数：" + data, var15);
            throw var15;
        } finally {
            close(is);

            try {
                client.close();
            } catch (Exception var14) {
                logger.warn("Http发送put关闭连接流错误");
            }

        }

        logger.info("Http发送put请求，Url：" + url + "; 参数：" + data + "; 结果：" + strB.toString());
        return strB.toString();
    }

    public static String delete(String url, JSONObject header) {
        logger.info("Http发送delete请求，Url：" + url);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        InputStream is = null;
        StringBuffer strB = new StringBuffer();

        label84: {
            Object var6;
            try {
                is = requestDelete(client, url, header);
                byte[] bts = new byte[1024];
                boolean var18 = true;

                int len;
                while((len = is.read(bts)) != -1) {
                    strB.append(new String(bts, 0, len, "utf-8"));
                }

                is.close();
                client.close();
                break label84;
            } catch (Exception var16) {
                logger.error("Http发送delete请求错误，Url：" + url, var16);
                var6 = null;
            } finally {
                close(is);

                try {
                    client.close();
                } catch (Exception var15) {
                    logger.warn("Http发送delete关闭连接流错误");
                }

            }

            return (String)var6;
        }

        logger.info("Http发送delete请求，Url：" + url + "; 结果：" + strB.toString());
        return strB.toString();
    }

    public static String post(String url, JSONObject data, JSONObject header) throws Exception {
        logger.info("Http发送post请求，Url：" + url + "; 参数：" + data);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        InputStream is = null;
        StringBuffer strB = new StringBuffer();

        try {
            is = requestPost(client, url, data, header);
            byte[] bts = new byte[1024];
            boolean var7 = true;

            while(true) {
                int len;
                if ((len = is.read(bts)) == -1) {
                    is.close();
                    client.close();
                    break;
                }

                strB.append(new String(bts, 0, len, "utf-8"));
            }
        } catch (Exception var15) {
            logger.error("Http发送post请求错误，Url：" + url + "; 参数：" + data, var15);
            throw var15;
        } finally {
            close(is);

            try {
                client.close();
            } catch (Exception var14) {
                logger.warn("Http发送post关闭连接流错误");
            }

        }

        logger.info("Http发送post请求，Url：" + url + "; 参数：" + data + "; 结果：" + strB.toString());
        return strB.toString();
    }

    public static String post(String url, String data) throws Exception {
        logger.info("Http发送Post请求，Url：" + url + "; 参数：" + data);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        InputStream is = null;
        StringBuffer strB = new StringBuffer();

        try {
            is = requestPost(client, url, data);
            byte[] bts = new byte[1024];
            boolean var6 = true;

            while(true) {
                int len;
                if ((len = is.read(bts)) == -1) {
                    is.close();
                    client.close();
                    break;
                }

                strB.append(new String(bts, 0, len, "utf-8"));
            }
        } catch (Exception var14) {
            logger.error("Http发送Post请求错误，Url：" + url + "; 参数：" + data, var14);
            throw var14;
        } finally {
            close(is);

            try {
                client.close();
            } catch (Exception var13) {
                logger.warn("Http发送Post关闭连接流错误");
            }

        }

        logger.info("Http发送Post请求，Url：" + url + "; 参数：" + data + "; 结果：" + strB.toString());
        return strB.toString();
    }

    public static String post(String url) throws Exception {
        logger.info("Http发送Post请求，Url：" + url);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        InputStream is = null;
        StringBuffer strB = new StringBuffer();

        try {
            is = requestPost(client, url, (String)null);
            byte[] bts = new byte[1024];
            boolean var5 = true;

            while(true) {
                int len;
                if ((len = is.read(bts)) == -1) {
                    is.close();
                    client.close();
                    break;
                }

                strB.append(new String(bts, 0, len, "utf-8"));
            }
        } catch (Exception var13) {
            logger.error("Http发送Post请求错误，Url：" + url, var13);
            throw var13;
        } finally {
            close(is);

            try {
                client.close();
            } catch (Exception var12) {
                logger.warn("Http发送Post关闭连接流错误");
            }

        }

        logger.info("Http发送Post请求，Url：" + url + "; 结果：" + strB.toString());
        return strB.toString();
    }

    public static String post(String url, Map<String, Object> param) throws Exception {
        logger.info("Http发送Post请求，Url：" + url + "; 参数：" + (new JSONObject(param)).toString());
        Iterator ite = param.keySet().iterator();
        StringBuffer strB = new StringBuffer();

        while(ite.hasNext()) {
            String key = (String)ite.next();
            String value = String.valueOf(param.get(key));
            strB.append(key).append("=").append(value);
            if (ite.hasNext()) {
                strB.append("&");
            }
        }

        InputStream is = null;
        CloseableHttpClient client = null;
        StringBuffer result = new StringBuffer();

        try {
            HttpPost post = new HttpPost(url);
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");
            post.setConfig((RequestConfig)commReqCfg.get());
            HttpEntity entity = new StringEntity(strB.toString());
            post.setEntity(entity);
            client = HttpClientBuilder.create().build();
            CloseableHttpResponse response = client.execute(post);
            is = response.getEntity().getContent();
            String str = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));

            while((str = br.readLine()) != null) {
                result.append(str);
            }
        } catch (Exception var15) {
            logger.error("Http发送Post请求错误，Url：" + url + "; 参数：" + (new JSONObject(param)).toString(), var15);
        } finally {
            close(is);
            close(client);
        }

        logger.info("Http发送Post请求，Url：" + url + "; 参数：" + (new JSONObject(param)).toString() + "; 结果：" + strB.toString());
        return result.toString();
    }

    private static void close(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (Exception var2) {
                logger.error("关闭Http连接失败", var2);
            }
        }

    }

    public static String getStringByBean(Object object) {
        try {
            if (null == object) {
                logger.warn("获取bean中的所有属性进行拼装URL失败, 参数为空{}", object);
                return null;
            } else {
                StringBuffer sb = new StringBuffer();
                Field[] fields = object.getClass().getDeclaredFields();
                Field[] var3 = fields;
                int var4 = fields.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    Field f = var3[var5];
                    f.setAccessible(true);
                    Object strSql = f.get(object);
                    sb.append("&");
                    sb.append(f.getName());
                    sb.append("=");
                    sb.append(strSql == null ? "" : strSql + "");
                }

                return sb.toString();
            }
        } catch (Exception var8) {
            logger.error("获取bean中的所有属性进行拼装URL失败", var8);
            return null;
        }
    }

    public static int getGetState(String url) throws Exception {
        logger.info("Http发送Get请求，Url：" + url);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        boolean var2 = true;

        int code;
        try {
            code = requestGetState(client, url, (Map)null);
        } catch (Exception var11) {
            logger.error("Http发送Get请求错误，Url：" + url, var11);
            throw var11;
        } finally {
            try {
                client.close();
            } catch (Exception var10) {
                logger.warn("Http发送Get关闭连接错误");
            }

        }

        return code;
    }

    private static int requestGetState(CloseableHttpClient client, String url, Map<String, Object> paramMap) throws Exception {
        URIBuilder uriB = new URIBuilder(url);
        if (paramMap != null && !paramMap.isEmpty()) {
            Iterator ite = paramMap.keySet().iterator();

            while(ite.hasNext()) {
                String key = (String)ite.next();
                uriB.setParameter(key, String.valueOf(paramMap.get(key)));
            }
        }

        HttpGet get = new HttpGet(uriB.build());
        get.setConfig((RequestConfig)commReqCfg.get());
        CloseableHttpResponse response = client.execute(get);
        return response.getStatusLine().getStatusCode();
    }
}