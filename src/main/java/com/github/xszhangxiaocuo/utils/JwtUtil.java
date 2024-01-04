package com.github.xszhangxiaocuo.utils;

import com.github.xszhangxiaocuo.entity.Token;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

public class JwtUtil {
    static Logger logger = Logger.getLogger(JwtUtil.class.getName());
    /**
     * JWT token 签名
     * 签名密钥长度至少32位
     */
    private static final String JWT_SIGN_KEY = "xszhangxiaocuo_jwt_token_json_sign_key";
    //签名密钥
    private static final String BASE64_SECURITY = Base64.getEncoder().encodeToString(JWT_SIGN_KEY.getBytes(StandardCharsets.UTF_8));


    /***
     * 创建令牌
     */
    public static Token generateJwt(Map<String, String> user, long expire) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;//设置签名算法
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //生成签名密钥
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(BASE64_SECURITY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加JWT的类，设置 JWT 的头部参数，声明这个令牌是一个 JSON Web Token
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JsonWebToken");

        //设置参数到jwt，对每一个user中对元素调用builder.claim()方法，将多个声明添加到jwt中
        user.forEach(builder::claim);

        //Token过期时间
        long expMillis = nowMillis + expire * 1000;
        Date exp = new Date(expMillis);
        //设置 JWT 的发布时间（issuedAt）、生效时间（notBefore）和过期时间（expiration），然后使用指定的签名密钥和算法进行签名
        builder
                .setIssuedAt(now)
                .setNotBefore(now)
                .setExpiration(exp)
                .signWith(signingKey, signatureAlgorithm);

        //组装Token信息
        Token tokenInfo = new Token();
        tokenInfo.setToken(builder.compact());
        tokenInfo.setExpire(expire);
        tokenInfo.setExpiration(localDateTime(exp));
        return tokenInfo;
    }

    /**
     * Date转换为LocalDateTime
     */
    public static LocalDateTime localDateTime(Date date) {
        if (date == null) {
            return LocalDateTime.now();
        }
        //获取时间戳
        Instant instant = date.toInstant();
        //获取当前系统时区
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    /***
     * 获取Claims
     */
    public static Claims getClaims(String token, long allowedClockSkewSeconds)throws ExpiredJwtException, SignatureException, IllegalArgumentException, Exception {
        if (token==null||token.isEmpty()) {
            return null;
        }

        return parseJwt(token, allowedClockSkewSeconds);
    }

    /***
     *  解析jwt
     */
    public static Claims parseJwt(String jsonWebToken, long allowedClockSkewSeconds) throws ExpiredJwtException, SignatureException, IllegalArgumentException, Exception{
        try {
            //JWT解析器
            return Jwts.parser()
                    .setSigningKey(Base64.getDecoder().decode(BASE64_SECURITY))//设置签名密钥，用于验证JWT签名
                    .setAllowedClockSkewSeconds(allowedClockSkewSeconds)//设置允许的时钟偏差秒数，这可以用来处理因服务器时间差异导致的微小时间差异问题。
                    .build()//构建解析器
                    .parseClaimsJws(jsonWebToken)//解析传入的 JWT 字符串
                    .getBody();//获取其主体
        } catch (ExpiredJwtException ex) {
            //过期
            //抛异常 让系统捕获到返回到前端
            logger.severe("token过期"+ex);
            throw ex;
        } catch (SignatureException ex) {
            //签名错误
            logger.severe("签名错误"+ ex);
            throw ex;
        } catch (IllegalArgumentException ex) {
            //token 为空
            logger.severe("token为空"+ex);
            throw ex;
        } catch (Exception e) {
            //token异常
            logger.severe("解析token异常"+e);
            throw e;
        }
    }

}
