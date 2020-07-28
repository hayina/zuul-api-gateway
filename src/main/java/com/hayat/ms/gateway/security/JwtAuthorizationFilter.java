package com.hayat.ms.gateway.security;

import com.hayat.ms.gateway.services.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private @Autowired JwtService jwtService;

    @Value("${security.token.header-string}")
    String tokenHeader;

    @Value("${security.token.prefix}")
    String tokenPrefix;


//    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
//        super(authenticationManager);
//    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String token = request.getHeader(tokenHeader);


        if (token != null && token.startsWith(tokenPrefix)) {

            UsernamePasswordAuthenticationToken authentication = null;
            Claims claims = jwtService.resolveClaimsFromToken(token);

            if (claims.getSubject() != null && claims.get("permissions") != null) {

//                Collection<GrantedAuthority> authorities = Arrays
//                        .stream(claims.get("permissions").toString().split(","))
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());


                authentication = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(),
                        null,
                        new ArrayList<>()
//                        authorities
                );


            }

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);

    }


}
