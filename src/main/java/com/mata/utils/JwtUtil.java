package com.mata.utils;

import com.mata.enumPackage.Role;
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
     *  创建 token
     *  @param id 用户id
     */
    public String createToken(Integer id, Role role){
        Map<String, Object> data=new HashMap<>();
        if(role.equals(Role.User)){
            data.put("userId",id);
        }else {
            data.put("adminId",id);
        }
        // 加密方式
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //创建JWT 生成token
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(signatureAlgorithm, key.getBytes()) //设置加密方式和密钥
                .setClaims(data) // 设置自己的数据
                .setExpiration(new Date(System.currentTimeMillis()+ttl)); // 设置过期时间
        return jwtBuilder.compact();
    }

    /**
     *  token解密
     *  @param token 解密
     */
    public Integer parseToken(String token,Role role){
        try {
            // 开始解析
            Claims claims = Jwts.parser()
                    .setSigningKey(key.getBytes(StandardCharsets.UTF_8)) //设置密钥
                    .parseClaimsJws(token).getBody();
            //获取数据
            if(role.equals(Role.User)){
                return Integer.valueOf(claims.get("userId").toString());
            }else {
                return Integer.valueOf(claims.get("adminId").toString());
            }

        } catch (Exception e){
            return null;
        }

    }




}
