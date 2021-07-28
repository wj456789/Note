package com.shzx.application.common.tool;

import com.alibaba.fastjson.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;

public class ToolMapBd {
    static final String URL_API_MAP_BAIDU = "http://api.map.baidu.com/place/v2/suggestion?";

    public ToolMapBd() {
    }

    public static void main(String[] args) {
        String sk = "NOu4dfFl3rFeS7XL5ikjrzpejlTjvfYR";
        String ak = "VndqEdIOh2Iuv0dT7PsUFjoI26Eis4ip";
        System.out.println(queryAddressTip("街道", "南京市", ak, sk));
    }

    public static JSONObject queryAddressTip(String query, String ak, String sk) {
        return queryAddressTip(query, (String)null, ak, sk);
    }

    public static JSONObject queryAddressTip(String query, String region, String ak, String sk) {
        try {
            Map<String, Object> paramsMap = new LinkedHashMap();
            String time = ToolTime.getTimestamp().getTime() + "";
            paramsMap.put("query", query);
            if (StringUtils.isNotBlank(region)) {
                paramsMap.put("region", region);
                paramsMap.put("city_limit", "true");
            }

            paramsMap.put("output", "json");
            paramsMap.put("timestamp", time);
            paramsMap.put("ak", ak);
            String sn = createSn(paramsMap, ak, sk);
            paramsMap.put("sn", sn);
            String str = ToolHttp.get("http://api.map.baidu.com/place/v2/suggestion?", paramsMap);
            System.out.println(str);
            if (StringUtils.isNotBlank(str)) {
                return JSONObject.parseObject(str);
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return null;
    }

    public static String createSn(Map<String, Object> paramsMap, String ak, String sk) {
        try {
            if (null == paramsMap) {
                paramsMap = new LinkedHashMap();
            }

            ((Map)paramsMap).put("ak", ak);
            String paramsStr = toQueryString((Map)paramsMap);
            String wholeStr = new String("/place/v2/suggestion?" + paramsStr + sk);
            String tempStr = URLEncoder.encode(wholeStr, "UTF-8");
            return MD5(tempStr);
        } catch (UnsupportedEncodingException var6) {
            var6.printStackTrace();
            return null;
        }
    }

    private static String toQueryString(Map<?, ?> data) throws UnsupportedEncodingException {
        StringBuffer queryString = new StringBuffer();
        Iterator var2 = data.entrySet().iterator();

        while(var2.hasNext()) {
            Entry<?, ?> pair = (Entry)var2.next();
            queryString.append(pair.getKey() + "=");
            queryString.append(URLEncoder.encode((String)pair.getValue(), "UTF-8") + "&");
        }

        if (queryString.length() > 0) {
            queryString.deleteCharAt(queryString.length() - 1);
        }

        return queryString.toString();
    }

    private static String MD5(String md5) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();

            for(int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString(array[i] & 255 | 256).substring(1, 3));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException var5) {
            var5.printStackTrace();
            return null;
        }
    }
}