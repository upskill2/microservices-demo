package com.microservices.demo.kafka.admin.konfig.clients;

import com.microservices.demo.common.config.KafkaConfigData;
import com.microservices.demo.common.config.RetryConfigData;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class KafkaAdminClient {

    private static final Logger LOG = Logger.getLogger (KafkaAdminClient.class.getName ());

    private final KafkaConfigData kafkaConfigData;
    private final RetryConfigData retryConfigData;
    private final AdminClient adminClient;
    private final RetryTemplate retryTemplate;


    public KafkaAdminClient (final KafkaConfigData kafkaConfigData, final AdminClient adminClient, final RetryConfigData retryConfigData, final RetryTemplate retryTemplate) {
        this.kafkaConfigData = kafkaConfigData;
        this.adminClient = adminClient;
        this.retryConfigData = retryConfigData;
        this.retryTemplate = retryTemplate;
    }


}
