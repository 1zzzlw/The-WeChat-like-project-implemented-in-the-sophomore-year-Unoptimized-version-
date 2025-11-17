package com.zzzlew.zzzimserver.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/7 - 11 - 07 - 0:06
 * @Description: com.zzzlew.zzzimserver.utils
 * @version: 1.0
 */
@Component
public class JwtUtil {

    // 生成token
    public String createJWT(String secretKey, long expiration, Map<String, Object> claims) {
        // 指定签名的时候使用的签名算法，也就是header那部分
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long expirationTime = System.currentTimeMillis() + expiration;
        Date exp = new Date(expirationTime);

        JwtBuilder builder = Jwts.builder().setClaims(claims).setExpiration(exp).signWith(signatureAlgorithm,
            secretKey.getBytes(StandardCharsets.UTF_8));

        return builder.compact();
    }

    // 解析token
    public Claims parseJWT(String secretKey, String token) {
        Claims claims =
            Jwts.parser().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody();
        return claims;
    }

    // 校验token是否过期
    public boolean isTokenExpired(String secretKey, String token) {
        Claims claims = parseJWT(secretKey, token);
        Date expiration = claims.getExpiration();
        // 获取当前时间
        Date now = new Date();
        // 如果过期时间在当前时间之前，则token过期
        return expiration.before(now);
    }

    // token的距离过期还剩多长时间
    public long getExpirationTime(String secretKey, String token) {
        Claims claims = parseJWT(secretKey, token);
        Date expiration = claims.getExpiration();
        // 获取当前时间
        Date now = new Date();
        // 过期时间减去当前时间，就是距离过期还剩多长时间
        long remainMinutes = (expiration.getTime() - now.getTime()) / 60000;
        return remainMinutes;
    }

}
