package com.microservices.demo.elastic.query.service.impl;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.client.service.ElasticQueryClient;
import com.microservices.demo.elastic.query.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.query.service.ElasticQueryService;
import com.microservices.demo.elastic.query.transformer.ElasticToResponseModelTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ElasticQueryServiceImpl implements ElasticQueryService {

    private final ElasticToResponseModelTransformer transformer;
    private final ElasticQueryClient<TwitterIndexModel> queryClient;

    public ElasticQueryServiceImpl (final ElasticToResponseModelTransformer transformer, final ElasticQueryClient<TwitterIndexModel> queryClient) {
        this.transformer = transformer;
        this.queryClient = queryClient;
    }

    @Override
    public ElasticQueryServiceResponseModel getDocumentById (final String id) {
        log.info ("Retrieving document with id: {}", id);
        return transformer.getResponseModel (queryClient.getIndexModelById (id));
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getDocumentByText (final String text) {
        log.info ("Retrieving documents with text: {}", text);
        return transformer.getResponseModels (queryClient.getIndexModelByText (text));
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getAllDocuments () {
        final List<TwitterIndexModel> allIndexModels = queryClient.getAllIndexModels ();
        log.info ("Retrieving all documents {}", allIndexModels.size ());
        return transformer.getResponseModels (allIndexModels);
    }
}
