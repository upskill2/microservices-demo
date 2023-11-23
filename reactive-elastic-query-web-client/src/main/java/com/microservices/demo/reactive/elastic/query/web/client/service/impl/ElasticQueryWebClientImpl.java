package com.microservices.demo.reactive.elastic.query.web.client.service.impl;

import com.microservices.demo.config.ElasticQueryWebClientConfigData;
import com.microservices.demo.elastic.query.web.client.common.exception.ElasticQueryWebClientException;
import com.microservices.demo.elastic.query.web.client.common.model.ElasticQueryWebClientRequestModel;
import com.microservices.demo.elastic.query.web.client.common.model.ElasticQueryWebClientResponseModel;
import com.microservices.demo.reactive.elastic.query.web.client.service.ElasticQueryWebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ElasticQueryWebClientImpl implements ElasticQueryWebClient {

    private final WebClient webClient;
    private final ElasticQueryWebClientConfigData elasticQueryWebClientConfig;

    public ElasticQueryWebClientImpl (@Qualifier("webClient") WebClient webClient,
                                      final ElasticQueryWebClientConfigData configData) {
        this.webClient = webClient;
        this.elasticQueryWebClientConfig = configData;
    }



    @Override
    public Flux<ElasticQueryWebClientResponseModel> getDataByText (ElasticQueryWebClientRequestModel requestModel) {
        log.info ("Querying by text {}", requestModel.getText ());
        return getWebClient (requestModel)
                .bodyToFlux (ElasticQueryWebClientResponseModel.class);
    }

    private WebClient.ResponseSpec getWebClient (ElasticQueryWebClientRequestModel requestModel) {
        return webClient
                .method (HttpMethod.valueOf (elasticQueryWebClientConfig.getQuery ().getMethod ()))
                .uri (elasticQueryWebClientConfig.getQuery ().getUri ())
                .accept (MediaType.valueOf (elasticQueryWebClientConfig.getQuery ().getAccept ()))
                .body (BodyInserters.fromPublisher (Mono.just (requestModel), createParameterizedTypeReference ()))
                .retrieve ()
                .onStatus (
                        httpStatus -> httpStatus.equals (HttpStatus.UNAUTHORIZED),
                        clientResponse -> Mono.just (new BadCredentialsException ("Not authenticated!")))
                .onStatus (
                        HttpStatus::is4xxClientError,
                        cr -> Mono.just (new ElasticQueryWebClientException (cr.statusCode ().getReasonPhrase ())))
                .onStatus (
                        HttpStatus::is5xxServerError,
                        cr -> Mono.just (new Exception (cr.statusCode ().getReasonPhrase ())));
    }


    private <T> ParameterizedTypeReference<T> createParameterizedTypeReference () {
        return new ParameterizedTypeReference<> () {
        };
    }
}
