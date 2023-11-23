package com.microservices.demo.reactive.elastic.query.service.business;

import com.microservices.demo.common.model.ElasticQueryServiceResponseModel;
import reactor.core.publisher.Flux;

public interface ElasticQueryService {

    Flux<ElasticQueryServiceResponseModel> getTweetsByText(String text);
}
