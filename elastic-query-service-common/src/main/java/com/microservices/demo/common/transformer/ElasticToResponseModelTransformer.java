package com.microservices.demo.common.transformer;

import com.microservices.demo.common.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ElasticToResponseModelTransformer {

    public ElasticQueryServiceResponseModel getResponseModel (TwitterIndexModel twitterIndexModel) {
        return ElasticQueryServiceResponseModel.builder ()
                .id (twitterIndexModel.getId ())
                .userId (twitterIndexModel.getUserId ())
                .text (twitterIndexModel.getText ())
                .createdAt (twitterIndexModel.getCreatedAt ())
                .build ();
    }

    public List<ElasticQueryServiceResponseModel> getResponseModels (List<TwitterIndexModel> twitterIndexModels) {

        return twitterIndexModels.stream ()
                .map (this::getResponseModel)
                .collect (java.util.stream.Collectors.toList ());
    }


}
