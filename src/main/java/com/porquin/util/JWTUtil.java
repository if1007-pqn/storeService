package com.porquin.util;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTUtil {

    @Value("${spring.secretKey}")
    private static String key = "oiqWe.jsnsSfxzcbvASOI!#A";

    public static final String TOKEN_HEADER = "Authentication";

    public static String create(String id, String owner) {
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.MONTH, 2);
        d = c.getTime();
        
        return Jwts.builder()
                .setAudience(owner)
                .setId(id)
                .setExpiration(d)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    public static Jws<Claims> decode(String token){
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token);
    }
}