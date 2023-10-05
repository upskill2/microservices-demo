package com.microservices.demo.twitter.to.kafka.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Configuration
public class SocialConfig {

    @Autowired
    private Environment environment;

    @Bean
    public void stp (    ) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("your consumer key")
                .setOAuthConsumerSecret("your consumer secret")
                .setOAuthAccessToken("your access token")
                .setOAuthAccessTokenSecret("your access token secret");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
    }

}