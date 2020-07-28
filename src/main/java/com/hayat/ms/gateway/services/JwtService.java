package com.hayat.ms.gateway.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${security.token.secret-key}")
    String secretKey;

    @Value("${security.token.prefix}")
    String tokenPrefix;

    public Claims resolveClaimsFromToken(String token) {

        return Jwts.parser()
                .setSigningKey( secretKey )
                .parseClaimsJws( token.replace(tokenPrefix,""))
                .getBody();
    }
}
