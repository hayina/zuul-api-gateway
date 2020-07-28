package com.hayat.ms.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    Environment environment;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.logout().disable()
                .formLogin().disable();

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST , environment.getProperty("api.login.url")).permitAll()
                .antMatchers("/project-app/api/**").authenticated()
                .antMatchers("/**").permitAll()

                .and()

                .addFilterBefore(getJwtAuthorizationFilter(), AnonymousAuthenticationFilter.class)

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ;
    }


    @Bean
    public JwtAuthorizationFilter getJwtAuthorizationFilter() {
        var jwtAuthorizationFilter = new JwtAuthorizationFilter();
        return jwtAuthorizationFilter;
//        return new JwtAuthorizationFilter(authenticationManager());
    }
}
