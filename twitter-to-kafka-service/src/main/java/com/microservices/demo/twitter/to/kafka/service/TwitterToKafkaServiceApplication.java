package com.microservices.demo.twitter.to.kafka.service;

import com.microservices.demo.twitter.to.kafka.service.config.TwitterToKafkaServiceDataConfig;
import com.microservices.demo.twitter.to.kafka.service.runner.StreamRunnerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import twitter4j.Twitter;

@SpringBootApplication
@ComponentScan(basePackages = "com.microservices.demo")
public class TwitterToKafkaServiceApplication implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger (TwitterToKafkaServiceApplication.class);

    @Autowired
    private TwitterToKafkaServiceDataConfig twitterToKafkaServiceDataConfig;
    @Autowired
    private StreamRunnerImpl streamRunner;

    public static void main (String[] args) {
        SpringApplication.run (TwitterToKafkaServiceApplication.class, args);
    }

    @Override
    public void run (final String... args) throws Exception {
        LOG.info (twitterToKafkaServiceDataConfig.getWelcomeMessage ());
        LOG.info ("Loaded twitter keywords: {}", twitterToKafkaServiceDataConfig.getTwitterKeyWords ());
        streamRunner.start ();
    }

}
