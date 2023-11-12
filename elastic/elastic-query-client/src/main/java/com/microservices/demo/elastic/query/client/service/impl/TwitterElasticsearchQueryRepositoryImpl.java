package com.microservices.demo.elastic.query.client.service.impl;

import com.microservices.demo.common.util.CollectionUtil;
import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.client.exception.ElasticQueryClientException;
import com.microservices.demo.elastic.query.client.repository.TwitterElasticsearchQueryRepository;
import com.microservices.demo.elastic.query.client.service.ElasticQueryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//@Primary
@Service
@Slf4j
@ConditionalOnProperty (name = "elastic-config.use-repository", havingValue = "true", matchIfMissing = true)
//@DependsOn ({"twitterElasticsearchQueryRepositoryImpl"})
public class TwitterElasticsearchQueryRepositoryImpl implements ElasticQueryClient<TwitterIndexModel> {

    private final TwitterElasticsearchQueryRepository repository;

    public TwitterElasticsearchQueryRepositoryImpl (final TwitterElasticsearchQueryRepository repository) {
        this.repository = repository;
    }

    @Override
    public TwitterIndexModel getIndexModelById (final String id) {
        final Optional<TwitterIndexModel> searchResult = repository.findById (id);
        log.info ("Documents with id {} retrieved successfully", searchResult.orElseThrow (() ->
                new ElasticQueryClientException ("No document found at elasticsearch with id: " + id)).getId ());

        return searchResult.get ();
    }

    @Override
    public List<TwitterIndexModel> getIndexModelByText (final String text) {
        final List<TwitterIndexModel> searchResult = repository.findByText (text);
        log.info ("{} of documents with text {} retrieved successfully", searchResult.size (), text);
        return searchResult;
    }

    @Override
    public List<TwitterIndexModel> getAllIndexModels () {
        final Iterable<TwitterIndexModel> result = repository.findAll ();

        final List<TwitterIndexModel> listFromIterable = CollectionUtil.getInstance ().getListFromIterable (result);
        log.info ("{} of documents retrieved successfully", listFromIterable.size ());
        return listFromIterable;
    }
}
