package com.microservices.demo.common.config;

import com.microservices.demo.config.RetryConfigData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RetryConfig {

    private final RetryConfigData retryConfigData;

    public RetryConfig (final RetryConfigData retryConfigData) {
        this.retryConfigData = retryConfigData;
    }

    @Bean
    public RetryTemplate retryTemplate () {
        RetryTemplate retryTemplate = new RetryTemplate ();

        ExponentialBackOffPolicy exponentialBackOff = new ExponentialBackOffPolicy ();
        exponentialBackOff.setInitialInterval (retryConfigData.getInitialIntervalMs ());
        exponentialBackOff.setMaxInterval (retryConfigData.getMaxIntervalMs ());
        exponentialBackOff.setMultiplier (retryConfigData.getMultiplier ());

        retryTemplate.setBackOffPolicy (exponentialBackOff);
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy ();
        retryPolicy.setMaxAttempts (retryConfigData.getMaxAttempts ());

        retryTemplate.setRetryPolicy (retryPolicy);

        return retryTemplate;
    }
}
