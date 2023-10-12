package com.microservices.demo.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConfigurationProperties (prefix = "kafka-config")
@Data
@Configuration
public class KafkaConfigData {

    private String bootstrapServers;
    private String schemaRegistryUrlKey;
    private String schemaRegistryUrl;
    private String topicName;
    private List<String> topicsToCreate;
    private Integer numOfPartitions;
    private Short replicationFactor;

}
