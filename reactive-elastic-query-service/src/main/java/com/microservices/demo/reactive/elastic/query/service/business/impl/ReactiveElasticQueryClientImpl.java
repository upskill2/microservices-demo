package com.microservices.demo.reactive.elastic.query.service.business.impl;

import com.microservices.demo.config.ElasticQueryServiceConfigData;
import com.microservices.demo.elastic.model.index.IndexModel;
import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.reactive.elastic.query.service.business.ReactiveElasticQueryClient;
import com.microservices.demo.reactive.elastic.query.service.repository.ElasticQueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
@Service
public class ReactiveElasticQueryClientImpl<T extends IndexModel> implements ReactiveElasticQueryClient<T> {

    private final ElasticQueryRepository elasticQueryRepository;
    private final ElasticQueryServiceConfigData elasticQueryServiceConfigData;

    public ReactiveElasticQueryClientImpl (final ElasticQueryRepository elasticQueryRepository,
                                           final ElasticQueryServiceConfigData elasticQueryServiceConfigData) {
        this.elasticQueryRepository = elasticQueryRepository;
        this.elasticQueryServiceConfigData = elasticQueryServiceConfigData;
    }

    @Override
    public Flux<TwitterIndexModel> getIndexModelByText (final String text) {
        log.info ("Querying elastic with text: {}", text);
        return elasticQueryRepository
                .findByText (text)
                .delayElements (Duration.ofMillis (elasticQueryServiceConfigData.getBackPressureDelayMs ()));
    }
}
