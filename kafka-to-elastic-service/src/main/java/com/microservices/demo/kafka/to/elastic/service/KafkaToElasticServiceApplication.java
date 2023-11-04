package com.microservices.demo.kafka.to.elastic.service;


import com.microservices.demo.kafka.to.elastic.service.consumer.TwitterToKafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.microservices.demo")
public class KafkaToElasticServiceApplication implements CommandLineRunner {

    @Autowired
    private TwitterToKafkaConsumer twitterToKafkaConsumer ;

    public static void main (String[] args) {
        SpringApplication.run(KafkaToElasticServiceApplication.class, args);
    }

    @Override
    public void run (final String... args) throws Exception {



    }
}
