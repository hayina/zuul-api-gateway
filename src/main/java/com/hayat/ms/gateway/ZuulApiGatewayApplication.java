package com.hayat.ms.gateway;

import com.hayat.ms.gateway.security.RolesForwarderFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class ZuulApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulApiGatewayApplication.class, args);
    }


    @Bean
    RolesForwarderFilter getRolesForwarderFilter(){
        return new RolesForwarderFilter();
    }

}
