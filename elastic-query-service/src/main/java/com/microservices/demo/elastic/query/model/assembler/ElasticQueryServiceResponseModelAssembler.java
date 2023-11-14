package com.microservices.demo.elastic.query.model.assembler;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.api.ElasticDocumentController;
import com.microservices.demo.elastic.query.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.query.transformer.ElasticToResponseModelTransformer;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ElasticQueryServiceResponseModelAssembler
        extends RepresentationModelAssemblerSupport<TwitterIndexModel, ElasticQueryServiceResponseModel> {


    private final ElasticToResponseModelTransformer transformer;

    public ElasticQueryServiceResponseModelAssembler (final ElasticToResponseModelTransformer transformer) {
        super (ElasticDocumentController.class, ElasticQueryServiceResponseModel.class);
        this.transformer = transformer;
    }

    @Override
    public ElasticQueryServiceResponseModel toModel (final TwitterIndexModel twitterIndexModel) {
        ElasticQueryServiceResponseModel responseModel =
                transformer.getResponseModel (twitterIndexModel);

        responseModel.add (
                linkTo (methodOn (ElasticDocumentController.class)
                        .getDocumentById (twitterIndexModel.getId ()))
                        .withSelfRel ());

        responseModel.add (
                linkTo (methodOn (ElasticDocumentController.class))
                        .withRel ("documents"));
        return responseModel;
    }

    public List<ElasticQueryServiceResponseModel> toModels (final List<TwitterIndexModel> twitterIndexModels) {
        return twitterIndexModels.stream ()
                .map (this::toModel)
                .collect(Collectors.toList());
    }

}
