package com.hayat.ms.gateway.security;

import com.hayat.ms.gateway.services.JwtService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;



public class RolesForwarderFilter extends ZuulFilter {

    @Autowired private Environment env;
    @Autowired JwtService jwtService;

    @Value("${security.token.header-string}")
    String tokenHeader;

    @Value("${security.token.prefix}")
    String tokenPrefix;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        var ctx = RequestContext.getCurrentContext();

        var request = ctx.getRequest();
        String token = request.getHeader(tokenHeader);

        if (token == null || !token.startsWith(tokenPrefix))
            return null;

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Claims claims = jwtService.resolveClaimsFromToken(request.getHeader(tokenHeader));
        ctx.addZuulRequestHeader(
                env.getProperty("security.user.identifier.header-name"),
                claims.getSubject()
//                authentication.getPrincipal().toString()
        );


        ctx.addZuulRequestHeader(
                env.getProperty("security.user.roles.header-name"),
                claims.get("permissions").toString()
//                authentication.getAuthorities()
        );

        return null;
    }
}
