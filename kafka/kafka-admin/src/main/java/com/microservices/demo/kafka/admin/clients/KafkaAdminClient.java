package com.microservices.demo.kafka.admin.clients;

import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.config.RetryConfigData;
import com.microservices.demo.kafka.admin.exceptions.KafkaClientExceptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
@Slf4j
public class KafkaAdminClient {

    private final KafkaConfigData kafkaConfigData;
    private final RetryConfigData retryConfigData;
    private final AdminClient adminClient;
    private final RetryTemplate retryTemplate;
    private final WebClient webClient;


    public KafkaAdminClient (final KafkaConfigData kafkaConfigData, final AdminClient adminClient, final RetryConfigData retryConfigData,
                             final RetryTemplate retryTemplate, final WebClient webClient) {
        this.kafkaConfigData = kafkaConfigData;
        this.adminClient = adminClient;
        this.retryConfigData = retryConfigData;
        this.retryTemplate = retryTemplate;
        this.webClient = webClient;
    }

    public void createTopics () {
        CreateTopicsResult createTopicsResult;

        try {
            createTopicsResult = retryTemplate.execute (this::doCreateTopics);
        } catch (Throwable e) {
            throw new KafkaClientExceptions ("Reached max number of retry for creating kafka topic(s)", e);
        }
        checkTopicCreated ();
    }
    public void checkSchemaRegistry () {
        int retryCount = 1;
        Integer maxRetry = retryConfigData.getMaxAttempts ();
        Integer multiplier = retryConfigData.getMultiplier ();
        Long sleepTimsMs = retryConfigData.getMaxIntervalMs ();

        while (!getShemaRegistryStatus ().is2xxSuccessful ()) {
            checkMaxRetry (retryCount++, maxRetry);
            sleep (sleepTimsMs);
            sleepTimsMs *= multiplier;
        }

    }

    public void checkTopicCreated () {
        Collection<TopicListing> topics = getTopics ();
        int retryCount = 1;
        Integer maxRetry = retryConfigData.getMaxAttempts ();
        Integer multiplier = retryConfigData.getMultiplier ();
        Long sleepTimsMs = retryConfigData.getMaxIntervalMs ();

        for (String topic : kafkaConfigData.getTopicsToCreate ()) {
            while (!isTopicCreated (topics, topic)) {
                checkMaxRetry (retryCount++, maxRetry);
                sleep (sleepTimsMs);
                sleepTimsMs *= multiplier;
                topics = getTopics ();
            }
        }
    }

    private HttpStatus getShemaRegistryStatus () {
        try {
            return webClient
                    .method (HttpMethod.GET)
                    .uri (kafkaConfigData.getSchemaRegistryUrl ())
                    .exchangeToMono (clientResponse -> Mono.just (clientResponse.statusCode ()))
                    .block ();
        } catch (Exception e) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        }
    }

    private CreateTopicsResult doCreateTopics (RetryContext retryContext) {
        List<String> topicNames = kafkaConfigData.getTopicsToCreate ();
        log.info ("Creating: {} topics(s), attemp: {}", topicNames, retryContext.getRetryCount ());

        List<NewTopic> kafkaTopic = topicNames.stream ().map (topic -> new NewTopic (
                topic.trim (),
                kafkaConfigData.getNumOfPartitions (),
                kafkaConfigData.getReplicationFactor ()
        )).collect (Collectors.toList ());
        return adminClient.createTopics (kafkaTopic);
    }

    private void sleep (final Long sleepTimsMs) {
        try {
            Thread.sleep (sleepTimsMs);
        } catch (InterruptedException e) {
            throw new KafkaClientExceptions ("error while thread sleep");
        }
    }

    private void checkMaxRetry (final int i, final Integer maxRetry) {
        if (i > maxRetry) {
            throw new KafkaClientExceptions ("Reached max number of retries for reading kafka topic(s)");

        }
    }

    private boolean isTopicCreated (final Collection<TopicListing> topics, final String topic) {
        if (topics == null) {
            return false;
        }
        return topics.stream ().anyMatch (topicListing -> topicListing.name ().equals (topic));
    }


    private Collection<TopicListing> getTopics () {
        Collection<TopicListing> topics;
        try {
            topics = retryTemplate.execute (this::doGetTopics);
        } catch (Throwable e) {
            throw new KafkaClientExceptions ("Reached max number of retry for reading kafka topic(s)", e);
        }
        return topics;
    }

    private Collection<TopicListing> doGetTopics (RetryContext retryContext) {
        log.info ("Reading: {} topics(s), attemp: {}", kafkaConfigData.getTopicsToCreate ().toArray (),
                retryContext.getRetryCount ());
        Collection<TopicListing> topics = null;
        try {
            topics = adminClient.listTopics ().listings ().get ();
        } catch (InterruptedException | ExecutionException e) {
            throw new KafkaClientExceptions ("Kafka topics are absent", e);
        }
        if (topics != null) {
            topics.forEach (topic -> log.info ("Topic with name {}", topic.name ()));
        }
        return topics;
    }

}
