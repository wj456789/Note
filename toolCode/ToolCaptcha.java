package com.shzx.application.common.tool;

/**
 * User: gaomd
 * Data: 2018/11/12
 * Time: 17:21
 * Proj: 项目
 */
import cn.hutool.core.comparator.CompareUtil;
import org.patchca.filter.predefined.CurvesRippleFilterFactory;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.utils.encoder.EncoderHelper;
import org.patchca.word.RandomWordFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class ToolCaptcha {
    private static ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
    private static Random random = new Random();
    private static Logger logger = LoggerFactory.getLogger(ToolCaptcha.class);
    static {
        cs.setColorFactory(x -> {
            int[] c = new int[3];
            int i = random.nextInt(c.length);
            for (int fi = 0; fi < c.length; fi++) {
                if (fi == i) {
                    c[fi] = random.nextInt(71);
                } else {
                    c[fi] = random.nextInt(256);
                }
            }
            return new Color(c[0], c[1], c[2]);
        });
        RandomWordFactory wf = new RandomWordFactory();
        wf.setCharacters("23456789abcdefghigkmnpqrstuvwxyzABCDEFGHIGKLMNPQRSTUVWXYZ");
        wf.setMaxLength(4);
        wf.setMinLength(4);
        cs.setWordFactory(wf);
    }

    public static String crimg(HttpServletRequest request, HttpServletResponse response, int verificationNumber ){
        Integer number =verificationNumber;
        Integer width =200;
        Integer height = 60;
        String token = "";
        cs.setHeight(height);
        cs.setWidth(width);
        RandomWordFactory rwf = new RandomWordFactory();
        rwf.setMinLength(number);
        rwf.setMaxLength(number);
        cs.setWordFactory(rwf);
        cs.setFilterFactory(new CurvesRippleFilterFactory(cs.getColorFactory()));
        setResponseHeaders(response);
        ServletOutputStream responseOutputStream = null;
        try{
            responseOutputStream =response.getOutputStream();
            token = EncoderHelper.getChallangeAndWriteImage(cs, "png", responseOutputStream);
        }catch (Exception e){
            logger.error("SsoCaptcha crimg get responseOutputStream error:" +e);
        }finally {
            if(responseOutputStream != null){
                try {
                    responseOutputStream.close();
                } catch (IOException e) {
                    logger.error("SsoCaptcha crimg close responseOutputStream error:" +e);
                }
            }
        }
        return token;
    }

    protected static void setResponseHeaders(HttpServletResponse response) {
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        long time = System.currentTimeMillis();
        response.setDateHeader("Last-Modified", time);
        response.setDateHeader("Date", time);
        response.setDateHeader("Expires", time);
    }

}
