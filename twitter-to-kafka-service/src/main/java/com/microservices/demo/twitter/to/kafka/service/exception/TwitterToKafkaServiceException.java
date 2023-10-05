package com.microservices.demo.twitter.to.kafka.service.exception;

public class TwitterToKafkaServiceException extends RuntimeException {
    public TwitterToKafkaServiceException (final String s) {
        super (s);
    }
    public TwitterToKafkaServiceException (final Throwable cause) {
        super (cause);
    }

    public TwitterToKafkaServiceException (final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super (message, cause, enableSuppression, writableStackTrace);
    }
}
