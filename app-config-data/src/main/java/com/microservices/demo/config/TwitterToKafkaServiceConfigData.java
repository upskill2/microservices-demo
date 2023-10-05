package com.microservices.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties (prefix = "twitter-to-kafka-service")
@Data
public class TwitterToKafkaServiceConfigData {

    private List<String> twitterKeyWords;
    private String welcomeMessage;
    private String twitterV2BaseUrl;
    private String twitterV2RulesBaseUrl;
    private String twitterV2BearerToken;
    private Integer mockMinTweetLength;
    private Integer mockMaxTweetLength;
    private Boolean enableMockTweets;
    private Long mockSleepMs;
}
