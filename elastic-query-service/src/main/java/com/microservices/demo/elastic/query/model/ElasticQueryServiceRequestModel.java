package com.microservices.demo.elastic.query.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ElasticQueryServiceRequestModel {

    private String id;
    private String text;
}