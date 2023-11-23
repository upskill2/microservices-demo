package com.microservices.demo.elastic.query.service;

import com.microservices.demo.common.model.ElasticQueryServiceResponseModel;

import java.util.List;

public interface ElasticQueryService {

    ElasticQueryServiceResponseModel getDocumentById(String id);

    List<ElasticQueryServiceResponseModel> getDocumentByText(String text);

    List<ElasticQueryServiceResponseModel> getAllDocuments();

}
