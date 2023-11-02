package com.microservices.demo.kafka.to.elastic.service.consumer;

import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.kafka.admin.clients.KafkaAdminClient;
import com.microservices.demo.kafka.avro.model.TwitterAvroModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TwitterToKafkaConsumer implements KafkaConsumer<Long, TwitterAvroModel> {

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final KafkaAdminClient kafkaAdminClient;
    private final KafkaConfigData kafkaConfigData;

    public TwitterToKafkaConsumer (final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry, final KafkaAdminClient kafkaAdminClient, final KafkaConfigData kafkaConfigData) {
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaConfigData = kafkaConfigData;
    }

    @Override
    @KafkaListener (id = "twitterTopicListener", topics = "${kafka-config.topic-name}")
    public void receive (@Payload List<TwitterAvroModel> messages,
                         @Header (KafkaHeaders.RECEIVED_MESSAGE_KEY) List<Integer> keys,
                         @Header (KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                         @Header (KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info ("{} number of messages received with keys {}, partitions {} and offsets {}, " +
                        "sending it to elastic: Thread id {}",
                messages.size (),
                keys.toString (),
                partitions.toString (),
                offsets.toString (),
                Thread.currentThread ().getId ());
    }
}
