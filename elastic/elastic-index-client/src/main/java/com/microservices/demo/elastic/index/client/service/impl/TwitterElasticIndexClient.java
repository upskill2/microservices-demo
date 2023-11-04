package com.microservices.demo.elastic.index.client.service.impl;

import com.microservices.demo.elastic.index.client.service.ElasticIndexClient;
import com.microservices.demo.elastic.model.index.IndexModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TwitterElasticIndexClient<T extends IndexModel> implements ElasticIndexClient<T> {


    @Override
    public List<String> save (final List<T> documents) {
        return null;
    }
}
