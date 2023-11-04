package com.microservices.demo.elastic.index.client.service.impl;

import com.microservices.demo.elastic.index.client.repository.TwitterElasticsearchIndexRepository;
import com.microservices.demo.elastic.index.client.service.ElasticIndexClient;
import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty (name = "elastic-config.is-repository", havingValue = "true", matchIfMissing = true)
@Slf4j
public class TwitterElasticsearchRepositoryIndexClient implements ElasticIndexClient<TwitterIndexModel> {

    private final TwitterElasticsearchIndexRepository twitterElasticsearchIndexRepository;

    public TwitterElasticsearchRepositoryIndexClient (final TwitterElasticsearchIndexRepository twitterElasticsearchIndexRepository) {
        this.twitterElasticsearchIndexRepository = twitterElasticsearchIndexRepository;
    }

    @Override
    public List<String> save (final List<TwitterIndexModel> documents) {
        List<TwitterIndexModel> twitterIndexModels = (List<TwitterIndexModel>)
                twitterElasticsearchIndexRepository.saveAll (documents);
        List<String> ids = twitterIndexModels.stream ()
                .map (TwitterIndexModel::getId)
                .collect (java.util.stream.Collectors.toList ());
        log.info ("Documents indexed successfully with type: {} and ids: {}",
                TwitterIndexModel.class.getName (),
                ids);

        return ids;
    }
}
