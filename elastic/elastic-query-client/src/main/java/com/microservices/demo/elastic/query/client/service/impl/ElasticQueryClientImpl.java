package com.microservices.demo.elastic.query.client.service.impl;

import com.microservices.demo.config.ElasticConfigData;
import com.microservices.demo.config.ElasticQueryConfigData;
import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.client.util.ElasticQueryUtil;
import com.microservices.demo.elastic.query.client.exception.ElasticQueryClientException;
import com.microservices.demo.elastic.query.client.service.ElasticQueryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ElasticQueryClientImpl implements ElasticQueryClient<TwitterIndexModel> {

    private final ElasticConfigData elasticConfigData;
    private final ElasticQueryConfigData elasticQueryConfigData;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticQueryUtil<TwitterIndexModel> elasticQueryUtil;

    public ElasticQueryClientImpl (final ElasticConfigData elasticConfigData,
                                   final ElasticQueryConfigData elasticQueryConfigData,
                                   final ElasticsearchOperations elasticSearchOperations,
                                   final ElasticQueryUtil<TwitterIndexModel> elasticQueryUtil) {
        this.elasticConfigData = elasticConfigData;
        this.elasticQueryConfigData = elasticQueryConfigData;
        this.elasticsearchOperations = elasticSearchOperations;
        this.elasticQueryUtil = elasticQueryUtil;
    }

    @Override
    public TwitterIndexModel getIndexModelById (final String id) {
        Query query = elasticQueryUtil.getSearchQueryById (id);
        SearchHit<TwitterIndexModel> searchResult = elasticsearchOperations.searchOne (query, TwitterIndexModel.class,
                IndexCoordinates.of (elasticConfigData.getIndexName ()));

        if (searchResult == null) {
            log.error ("No document found at elasticsearch with id: {}", id);
            throw new ElasticQueryClientException ("No document found at elasticsearch with id: " + id);
        }
        log.info ("Found document at elasticsearch with id: {}", id);
        return searchResult.getContent ();
    }

    @Override
    public List<TwitterIndexModel> getIndexModelByText (final String text) {
        Query query = elasticQueryUtil.getSearchQueryByFieldText (elasticQueryConfigData.getTextField (), text);
        return search (query, "{} of documents with text {} retrieved successfully", text);
    }

    private List<TwitterIndexModel> search (final Query query, final String logText, final Object... logParams) {
        SearchHits<TwitterIndexModel> searchResult = elasticsearchOperations.search (query, TwitterIndexModel.class,
                IndexCoordinates.of (elasticConfigData.getIndexName ()));
        log.info (logText, searchResult.getTotalHits (), logParams);

        return searchResult.get ().map (SearchHit::getContent).collect (Collectors.toList ());
    }

    @Override
    public List<TwitterIndexModel> getAllIndexModels () {
        Query query = elasticQueryUtil.getSearchQueryForAll ();
        return search (query, "Found {} documents at elasticsearch");
    }
}
