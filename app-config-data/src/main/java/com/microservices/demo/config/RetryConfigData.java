package com.microservices.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties (prefix = "retry-config")
@Data
public class RetryConfigData {

    private Long initialIntervalMs;
    private Long maxIntervalMs;
    private Integer multiplier;
    private Integer maxAttempts;
    private Long sleepTimeMs;

}
