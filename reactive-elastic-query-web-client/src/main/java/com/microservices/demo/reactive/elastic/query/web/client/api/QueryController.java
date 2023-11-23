package com.microservices.demo.reactive.elastic.query.web.client.api;

import com.microservices.demo.elastic.query.web.client.common.model.ElasticQueryWebClientRequestModel;
import com.microservices.demo.elastic.query.web.client.common.model.ElasticQueryWebClientResponseModel;
import com.microservices.demo.reactive.elastic.query.web.client.service.ElasticQueryWebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;

import javax.validation.Valid;

@Controller
@Slf4j
public class QueryController {

    private final ElasticQueryWebClient elasticQueryWebClient;

    public QueryController (final ElasticQueryWebClient elasticQueryWebClient) {
        this.elasticQueryWebClient = elasticQueryWebClient;
    }

    @GetMapping ("")
    public String index () {
        return "index";
    }

    public String home (Model model) {
        model.addAttribute ("elasticQueryResponseModel",
                ElasticQueryWebClientResponseModel.builder ().build ());
        return "home";
    }

    @GetMapping ("/error")
    public String error () {
        return "error";
    }

    @PostMapping (value = "/query-by-text")
    public String queryByText (@Valid ElasticQueryWebClientRequestModel requestModel, Model model) {

        Flux<ElasticQueryWebClientResponseModel> response = elasticQueryWebClient.getDataByText (requestModel);
        response = response.log ();

        IReactiveDataDriverContextVariable reactiveDataDrivenMode =
                new ReactiveDataDriverContextVariable (response, 1);
        model.addAttribute ("elasticQueryClientResponseModels", reactiveDataDrivenMode);
        model.addAttribute ("searchText", requestModel.getText ());
        model.addAttribute ("elasticQueryResponseModel",
                ElasticQueryWebClientResponseModel.builder ().build ());

        log.info ("Returning from reactive client controller for text {}", requestModel.getText ());

        return "home";

    }

}