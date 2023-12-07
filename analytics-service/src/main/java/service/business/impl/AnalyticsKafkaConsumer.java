package service.business.impl;

import com.microservices.demo.config.KafkaConfigData;

import com.microservices.demo.kafka.admin.clients.KafkaAdminClient;
import com.microservices.demo.kafka.avro.model.TwitterAnalyticsAvroModel;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import com.microservices.demo.kafka.consumer.api.KafkaConsumer;
import service.dataaccess.entity.AnalyticsEntity;
import service.dataaccess.repository.AnalyticsRepository;
import service.transformer.AvroToDbEntityModelTransformer;

import java.util.List;

@Service
@Slf4j
public class AnalyticsKafkaConsumer implements KafkaConsumer<TwitterAnalyticsAvroModel> {

    private static final Logger LOG = LoggerFactory.getLogger (AnalyticsKafkaConsumer.class);

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    private final KafkaAdminClient kafkaAdminClient;

    private final KafkaConfigData kafkaConfig;

    private final AvroToDbEntityModelTransformer avroToEntityModelTransformer;

    private final AnalyticsRepository analyticsRepository;

    public AnalyticsKafkaConsumer (KafkaListenerEndpointRegistry registry,
                                   KafkaAdminClient adminClient,
                                   KafkaConfigData config, AvroToDbEntityModelTransformer avroToEntityModelTransformer, AnalyticsRepository analyticsRepository) {
        this.kafkaListenerEndpointRegistry = registry;
        this.kafkaAdminClient = adminClient;
        this.kafkaConfig = config;
        this.avroToEntityModelTransformer = avroToEntityModelTransformer;
        this.analyticsRepository = analyticsRepository;
    }

    @EventListener
    public void onAppStarted (ApplicationStartedEvent event) {
        kafkaAdminClient.checkTopicCreated ();
        LOG.info ("Topics with name {} is ready for operations!", kafkaConfig.getTopicNamesToCreate ().toArray ());
        kafkaListenerEndpointRegistry.getListenerContainer ("twitterAnalyticsTopicListener").start ();
    }

    @Override
    @KafkaListener (id = "twitterAnalyticsTopicListener", topics = "${kafka-config.topic-name}", autoStartup = "false")
    public void receive (@Payload List<TwitterAnalyticsAvroModel> messages,
                         @Header (KafkaHeaders.RECEIVED_MESSAGE_KEY) List<Long> keys,
                         @Header (KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                         @Header (KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info ("{} number of messages received with keys {}, partitions {} and offsets {}, writing to DB",
                messages.size (), keys, partitions, offsets);

        List<AnalyticsEntity> twitterAnalyticsEntities = avroToEntityModelTransformer.getEntityModel (messages);
        analyticsRepository.batchPersist (twitterAnalyticsEntities);
        log.info ("Successfully wrote {} number of messages to DB", messages.size ());
    }

}
