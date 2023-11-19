package com.microservices.demo.config;

import lombok.SneakyThrows;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;

import java.io.IOException;
import java.util.Objects;

@Configuration
@EnableElasticsearchRepositories (basePackages = "com.microservices.demo")
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {

    private final ElasticConfigData elasticConfigData;

    public ElasticsearchConfig (final ElasticConfigData elasticConfigData) {
        this.elasticConfigData = elasticConfigData;
    }

    @SneakyThrows
    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient () {
        UriComponents serverUri = UriComponentsBuilder.fromHttpUrl (elasticConfigData.getConnectionUrl ()).build ();

        final RestHighLevelClient client = new RestHighLevelClient (
                RestClient.builder (new HttpHost (
                        Objects.requireNonNull (serverUri.getHost ()),
                        serverUri.getPort (),
                        serverUri.getScheme ()
                )).setRequestConfigCallback (
                        requestConfigBuilder -> requestConfigBuilder
                                .setConnectTimeout (elasticConfigData.getConnectionTimeoutMs ())
                                .setSocketTimeout (elasticConfigData.getSocketTimeoutMs ())
                ));
        setMaxWindowResult (client);
        queryBuilder (client);


        return  client;
    }

    private void queryBuilder (final RestHighLevelClient client) throws IOException {
        SearchRequest searchRequest = new SearchRequest(elasticConfigData.getIndexName ());

        // Create a search source builder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.matchAllQuery())
                .from(0) // Set the starting index for pagination
                .size(100); // Set the number of items to retrieve

        // Set the search source builder in the search request
        searchRequest.source(searchSourceBuilder);
        client.search(searchRequest, RequestOptions.DEFAULT);
    }

    private void setMaxWindowResult (final RestHighLevelClient client) throws IOException {
        UpdateSettingsRequest updateSettingsRequest = new UpdateSettingsRequest (elasticConfigData.getIndexName ())
                .settings (Settings.builder ()
                                .put ("index.max_result_window", 30000) // Set max_result_window
                        .put ("index.max_rescore_window", 400)
                        .put ("index.max_inner_result_window", 400)
                );
        client.indices ().putSettings (updateSettingsRequest, RequestOptions.DEFAULT);
    }

    @Bean
    public ElasticsearchOperations elasticsearchOperations () {
        return new ElasticsearchRestTemplate (elasticsearchClient ());
    }
}
