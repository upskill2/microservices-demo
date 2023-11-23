package com.microservices.demo.reactive.elastic.query.service.api;

import com.microservices.demo.common.model.ElasticQueryServiceRequestModel;
import com.microservices.demo.common.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.reactive.elastic.query.service.business.ElasticQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping (value = "/documents")
public class ElasticDocumentController {

    private final ElasticQueryService elasticQueryService;

    public ElasticDocumentController (final ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
    }


    @PostMapping (value = "/get-doc-by-text",
    produces = MediaType.TEXT_EVENT_STREAM_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ElasticQueryServiceResponseModel> getDocumentsByText (
            @RequestBody @Valid ElasticQueryServiceRequestModel requestModel) {
        log.info ("Querying elastic with text: {}", requestModel.getText ());
        final Flux<ElasticQueryServiceResponseModel> response = elasticQueryService.getTweetsByText (requestModel.getText ());
        response.log ();
        log.info ("Returning elastic with text: {} completed", requestModel.getText ());
        return response;
    }
}
