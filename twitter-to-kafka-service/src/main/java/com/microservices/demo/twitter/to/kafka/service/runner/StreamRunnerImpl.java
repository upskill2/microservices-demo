package com.microservices.demo.twitter.to.kafka.service.runner;

import com.microservices.demo.twitter.to.kafka.service.config.TwitterToKafkaServiceDataConfig;
import com.microservices.demo.twitter.to.kafka.service.listener.TwitterKafkaStatusListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.FilterQuery;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import javax.annotation.PreDestroy;

@Component
public class StreamRunnerImpl implements StreamRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger (StreamRunnerImpl.class);
    @Autowired
    private TwitterKafkaStatusListener twitterKafkaStatusListener;
    @Autowired
    private TwitterToKafkaServiceDataConfig twitterToKafkaServiceDataConfig;

    private TwitterStream twitterStream;

    @Override
    public void start () throws TwitterException {
        twitterStream = new TwitterStreamFactory ().getInstance ();
        twitterStream.addListener (twitterKafkaStatusListener);
        addFilter ();
    }

    @PreDestroy
    public void shutDown(){
        if(twitterStream !=null){
            LOGGER.info ("Closing twitter stream");
            twitterStream.shutdown ();
        }
    }

    private void addFilter () {
        String[] keywords = twitterToKafkaServiceDataConfig.getTwitterKeyWords ().toArray (new String[0]);
        FilterQuery filterQuery = new FilterQuery (keywords);
        twitterStream.filter (filterQuery);
        LOGGER.info ("Started filtering twitter stream for keywords {}", twitterToKafkaServiceDataConfig.getTwitterKeyWords ());
    }
}
