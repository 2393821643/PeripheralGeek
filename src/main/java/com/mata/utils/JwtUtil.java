package com.mata.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${jwt.admin-secret-key}")
    private String key;
    @Value("${jwt.admin-ttl}")
    private long ttl;

    /**
     *  创建user token
     *  @param userid 用户id
     */
    public String createToken(Integer userid){
        Map<String, Object> userData=new HashMap<>();
        userData.put("userId",userid);
        // 加密方式
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //创建JWT 生成token
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(signatureAlgorithm, key.getBytes()) //设置加密方式和密钥
                .setClaims(userData) // 设置自己的数据
                .setExpiration(new Date(System.currentTimeMillis()+ttl)); // 设置过期时间
        return jwtBuilder.compact();
    }

    /**
     *  user token解密
     *  @param token 解密
     */
    public Integer parseToken(String token){
        try {
            // 开始解析
            Claims claims = Jwts.parser()
                    .setSigningKey(key.getBytes(StandardCharsets.UTF_8)) //设置密钥
                    .parseClaimsJws(token).getBody();
            //获取数据
            return Integer.valueOf(claims.get("userId").toString());
        } catch (Exception e){
            return null;
        }

    }




}
