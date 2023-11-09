package com.microservices.demo.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "user-config")
@Data
public class UserConfig {

    private String username;
    private String password;
    private String[] role;
}
