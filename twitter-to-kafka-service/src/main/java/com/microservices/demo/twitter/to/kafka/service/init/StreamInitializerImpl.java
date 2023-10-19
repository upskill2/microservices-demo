package com.microservices.demo.twitter.to.kafka.service.init;

import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.kafka.admin.clients.KafkaAdminClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StreamInitializerImpl implements StreamInitializer {

    private static final Logger LOG = LoggerFactory.getLogger (StreamInitializerImpl.class);

    private final KafkaConfigData kafkaConfigData;
    private final KafkaAdminClient kafkaAdminClient;

    public StreamInitializerImpl (final KafkaConfigData kafkaConfigData, final KafkaAdminClient kafkaAdminClient) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaAdminClient = kafkaAdminClient;
    }

    @Override
    public void init () {
        kafkaAdminClient.createTopics ();
        kafkaAdminClient.checkSchemaRegistry ();
        LOG.info ("Topics with name {} is ready for operations", kafkaConfigData.getTopicNamesToCreate ().toArray ());

    }
}
