package com.microservices.demo.reactive.elastic.query.service.configs;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@Primary
public class WebSecurityConfig {
    @Bean (name = "webSecurityConfig")
    public SecurityWebFilterChain webSecurityConfig (ServerHttpSecurity httpSecurity) {

        httpSecurity.authorizeExchange ()
                .anyExchange ()
                .permitAll ();
        httpSecurity.csrf ().disable ();
        return httpSecurity.build ();
    }
}
