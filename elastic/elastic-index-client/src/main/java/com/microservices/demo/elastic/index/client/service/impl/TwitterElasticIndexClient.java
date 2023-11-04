package com.microservices.demo.elastic.index.client.service.impl;

import com.microservices.demo.config.ElasticConfigData;
import com.microservices.demo.elastic.index.client.service.ElasticIndexClient;
import com.microservices.demo.elastic.index.client.util.ElasticIndexUtil;
import com.microservices.demo.elastic.model.index.IndexModel;
import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty (name = "elastic-config.is-repository", havingValue = "false")
@Slf4j
public class TwitterElasticIndexClient<T extends IndexModel> implements ElasticIndexClient<T> {

    private final ElasticConfigData elasticConfigData;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticIndexUtil<T> elasticIndexUtil;

    public TwitterElasticIndexClient (final ElasticConfigData elasticConfigData, final ElasticsearchOperations elasticsearchOperations, final ElasticIndexUtil<T> elasticIndexUtil) {
        this.elasticConfigData = elasticConfigData;
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticIndexUtil = elasticIndexUtil;
    }

    @Override
    public List<String> save (final List<T> documents) {

        List<IndexQuery> indexQueries = elasticIndexUtil.getIndexQueries (documents);
        List<String> documentIds = elasticsearchOperations.bulkIndex (
                indexQueries,
                IndexCoordinates.of (elasticConfigData.getIndexName ())
        );
        log.info ("Documents indexed successfully with type: {} and ids: {}",
                TwitterIndexModel.class.getName (),
                documentIds);

        return documentIds;
    }
}
