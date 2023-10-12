package com.microservices.demo.kafka.admin.konfig;

import com.microservices.demo.common.config.KafkaConfigData;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

import java.util.Map;


@EnableRetry
@Configuration
public class KafkaAdminConfig {

    private final KafkaConfigData kafkaconfigData;

    public KafkaAdminConfig (final KafkaConfigData kafkaconfigData) {
        this.kafkaconfigData = kafkaconfigData;
    }


    @Bean
    public AdminClient adminClient () {
        return AdminClient.create (Map.of (CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG,
                kafkaconfigData.getBootstrapServers ()));
    }
}
