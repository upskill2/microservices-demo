package com.microservices.demo.kafka.producer.service;

import com.microservices.demo.kafka.avro.model.TwitterAvroModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.PreDestroy;

@Service
@Slf4j
public class TwitterKafkaProducer implements KafkaProducer<Long, TwitterAvroModel> {

    private final KafkaTemplate<Long, TwitterAvroModel> kafkaTemplate;

    public TwitterKafkaProducer (final KafkaTemplate<Long, TwitterAvroModel> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send (final String topicName, Long key, TwitterAvroModel message) {
        log.info ("Sending message {} to topic {}", message, topicName);

        ListenableFuture<SendResult<Long, TwitterAvroModel>> kafkaResultFuture
                = (ListenableFuture<SendResult<Long, TwitterAvroModel>>) kafkaTemplate.send (topicName, key, message);
        addCallBack (message, kafkaResultFuture);
    }

    @PreDestroy
    public void close () {
        if (kafkaTemplate != null) {
            log.info ("Closing kafka producer");
            kafkaTemplate.destroy ();
        }
    }

    private static void addCallBack (final TwitterAvroModel message, final ListenableFuture<SendResult<Long, TwitterAvroModel>> kafkaResultFuture) {
        kafkaResultFuture.addCallback (new ListenableFutureCallback<> () {
            @Override
            public void onFailure (final Throwable ex) {
                log.info ("Unable to send message=[ {} ] due to : {}", message, ex.getMessage (), ex);

            }

            @Override
            public void onSuccess (final SendResult<Long, TwitterAvroModel> result) {
                RecordMetadata metadata = result.getRecordMetadata ();
                log.info ("Received new metadata. Topic: {}; Partition {}; Offset {}; Timestamp {}",
                        metadata.topic (), metadata.partition (), metadata.offset (), metadata.timestamp ());
            }
        });
    }
}
