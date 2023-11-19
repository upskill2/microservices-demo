package com.microservices.demo.elastic.query.web.client.service.impl;

import com.microservices.demo.config.ElasticQueryWebClientConfigData;
import com.microservices.demo.elastic.query.web.client.exception.ElasticQueryWebClientException;
import com.microservices.demo.elastic.query.web.client.model.ElasticQueryWebClientRequestModel;
import com.microservices.demo.elastic.query.web.client.model.ElasticQueryWebClientResponseModel;
import com.microservices.demo.elastic.query.web.client.service.ElasticQueryWebClient;
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
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
public class ElasticQueryWebClientImpl implements ElasticQueryWebClient {

    private final WebClient.Builder webClientBuilder;
    private final ElasticQueryWebClientConfigData elasticQueryWebClientConfigData;

    public ElasticQueryWebClientImpl (@Qualifier ("webClientBuilder") WebClient.Builder webClientBuilder, ElasticQueryWebClientConfigData elasticQueryWebClientConfigData) {
        this.webClientBuilder = webClientBuilder;
        this.elasticQueryWebClientConfigData = elasticQueryWebClientConfigData;
    }


    @Override
    public List<ElasticQueryWebClientResponseModel> getDataByText (final ElasticQueryWebClientRequestModel requestModel) {
        log.info ("Querying by text: {}", requestModel.getText ());
        return  getWebClient (requestModel)
                .bodyToFlux (ElasticQueryWebClientResponseModel.class)
                .collectList ()
                .block ();

    }


    private WebClient.ResponseSpec getWebClient (ElasticQueryWebClientRequestModel requestModel) {
        return webClientBuilder
                .build ()
                .method (HttpMethod.valueOf (elasticQueryWebClientConfigData.getQuery ().getMethod ()))
                .uri (elasticQueryWebClientConfigData.getQuery ().getUri ())
                .accept (MediaType.valueOf (elasticQueryWebClientConfigData.getQuery ().getAccept ()))
                .body (BodyInserters.fromPublisher (Mono.just (requestModel), createParameterizedTypeReference ()))
                .retrieve ()
                .onStatus (httpStatus -> httpStatus == HttpStatus.UNAUTHORIZED,
                        clientResponse -> Mono.just (new BadCredentialsException ("Invalid credentials")))
                .onStatus (HttpStatus::is4xxClientError,
                        clientResponse -> Mono.just (new ElasticQueryWebClientException (clientResponse.statusCode ().getReasonPhrase ())))
                .onStatus (HttpStatus::is5xxServerError,
                        clientResponse -> Mono.just (new Exception (clientResponse.statusCode ().getReasonPhrase ())));
    }

    private <T> ParameterizedTypeReference<T> createParameterizedTypeReference () {
        return new ParameterizedTypeReference<> () {
        };
    }
}
