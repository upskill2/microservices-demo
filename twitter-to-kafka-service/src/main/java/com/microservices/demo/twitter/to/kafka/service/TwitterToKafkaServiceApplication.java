package com.microservices.demo.twitter.to.kafka.service;

import com.microservices.demo.twitter.to.kafka.service.config.TwitterToKafkaServiceConfigData;
import com.microservices.demo.twitter.to.kafka.service.runner.StreamRunner;
import com.microservices.demo.twitter.to.kafka.service.runner.impl.StreamRunnerImpl;
import com.microservices.demo.twitter.to.kafka.service.runner.impl.TwitterV2KafkaStreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan (basePackages = "com.microservices.demo")
public class TwitterToKafkaServiceApplication implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger (TwitterToKafkaServiceApplication.class);

    @Autowired
    private TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData;
    @Autowired
    private TwitterV2KafkaStreamRunner streamRunner;

    @Autowired
    private TwitterV2KafkaStreamRunner twitterV2KafkaStreamRunner;

    public static void main (String[] args) {
        SpringApplication.run (TwitterToKafkaServiceApplication.class, args);
    }

    @Override
    public void run (final String... args) throws Exception {
        LOG.info (twitterToKafkaServiceConfigData.getWelcomeMessage ());
        LOG.info ("Loaded twitter keywords: {}", twitterToKafkaServiceConfigData.getTwitterKeyWords ());

        streamRunner.start ();
    }

}
