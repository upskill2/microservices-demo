package com.microservices.demo.kafka.admin.exceptions;

public class KafkaClientExceptions extends RuntimeException {


    public KafkaClientExceptions () {
    }

    public KafkaClientExceptions (final String message) {
        super (message);
    }

    public KafkaClientExceptions (final String message, final Throwable cause) {
        super (message, cause);
    }
}
