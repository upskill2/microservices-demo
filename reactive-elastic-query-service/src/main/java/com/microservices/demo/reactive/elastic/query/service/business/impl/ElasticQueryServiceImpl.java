package com.microservices.demo.reactive.elastic.query.service.business.impl;

import com.microservices.demo.common.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.common.transformer.ElasticToResponseModelTransformer;
import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.reactive.elastic.query.service.business.ElasticQueryService;
import com.microservices.demo.reactive.elastic.query.service.business.ReactiveElasticQueryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class ElasticQueryServiceImpl implements ElasticQueryService {

    private final ReactiveElasticQueryClient<TwitterIndexModel> reactiveElasticQueryClient;

    private final ElasticToResponseModelTransformer elasticToResponseModelTransformer;

    public ElasticQueryServiceImpl (final ReactiveElasticQueryClient<TwitterIndexModel> reactiveElasticQueryClient, final ElasticToResponseModelTransformer elasticToResponseModelTransformer) {
        this.reactiveElasticQueryClient = reactiveElasticQueryClient;
        this.elasticToResponseModelTransformer = elasticToResponseModelTransformer;
    }

    @Override
    public Flux<ElasticQueryServiceResponseModel> getTweetsByText (final String text) {
        log.info ("Querying elastic with text: {}", text);
        return reactiveElasticQueryClient.getIndexModelByText (text)
                .map (elasticToResponseModelTransformer::getResponseModel);
    }
}
