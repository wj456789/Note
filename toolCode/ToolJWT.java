package com.shzx.application.common.tool;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Date;

/**
 * @author wj
 * @since 2018-05-07
 */
public class ToolJWT {

    // 过期时间1天
    private static final long EXPIRE_TIME = 24*60*60*1000;
    //需要重新生成的时间 如果token的时间超过这个 则重新生成token
    private static final long NEED_RESIGN_TIME = 3*60*60*1000;


    /**
     * 校验token是否正确
     * @param token 密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String userNo, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("userNo", userNo)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     * @return token中包含的用户名
     */
    public static String getUserNo(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userNo").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 判断token是否过期
     */
    public static Boolean isTimeValid(String token){
        DecodedJWT decodedJWT = JWT.decode(token);
        if(Date.from(Instant.now()).before(decodedJWT.getExpiresAt())){
            return true;
        }
        return false;
    }

    /**
     * 判断token是否需要重新签发
     */
    public static Boolean isNeedSign(String token){
        DecodedJWT decodedJWT = JWT.decode(token);
        Instant expiredInstant = decodedJWT.getExpiresAt().toInstant();
        Instant resignInstant = decodedJWT.getExpiresAt().toInstant().minusSeconds(NEED_RESIGN_TIME);
        if(Instant.now().isAfter(resignInstant)&&Instant.now().isBefore(expiredInstant)){
            return true;
        }
        return false;
    }

    /**
     * 生成签名,指定时间后过期,一经生成不可修改，令牌在指定时间内一直有效
     * @param userNo 用户编号
     * @param secret 用户的密码
     * @return 加密的token
     */
    public static String sign(String userNo, String secret) {
        try {
            Date date = new Date(System.currentTimeMillis()+EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带username信息
            return JWT.create()
                    .withClaim("userNo", userNo)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
