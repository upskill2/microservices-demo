package com.microservices.demo.elastic.query.api;

import com.microservices.demo.elastic.query.model.ElasticQueryServiceRequestModel;
import com.microservices.demo.elastic.query.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.query.model.ElasticQueryServiceResponseModelV2;
import com.microservices.demo.elastic.query.service.ElasticQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequestMapping (value = "/documents")
@Slf4j
public class ElasticDocumentController {

    private final ElasticQueryService queryService;

    public ElasticDocumentController (final ElasticQueryService queryService) {
        this.queryService = queryService;
    }


    @GetMapping ("/v1")
    public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>> getAllDocuments () {
        List<ElasticQueryServiceResponseModel> response = queryService.getAllDocuments ();
        log.info ("Elastic returned {} of documents", response.size ());
        return ResponseEntity.ok (response);
    }


    @GetMapping ("/v1/{id}")
    public @ResponseBody ResponseEntity<ElasticQueryServiceResponseModel>
    getDocumentById (@PathVariable @NotEmpty String id) {
        ElasticQueryServiceResponseModel response = queryService.getDocumentById (id);
        log.info ("Elastic returned document with id {}", id);
        return ResponseEntity.ok (response);
    }

    @GetMapping ("/v2/{id}")
    public @ResponseBody ResponseEntity<ElasticQueryServiceResponseModelV2>
    getDocumentByIdV2 (@PathVariable @NotEmpty String id) {
        ElasticQueryServiceResponseModel response = queryService.getDocumentById (id);
        ElasticQueryServiceResponseModelV2 responseV2 = getV2Model (response);
        log.info ("Elastic returned document with id {}", id);
        return ResponseEntity.ok (responseV2);
    }


    @PostMapping ("/v1/get-document-by-text")
    public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>> getDocumentsByText (
            @RequestBody @Valid ElasticQueryServiceRequestModel request) {
        List<ElasticQueryServiceResponseModel> response = queryService.getDocumentByText (request.getText ());
        log.info ("Elastic returned {} of documents", response.size ());
        return ResponseEntity.ok (response);
    }

    private ElasticQueryServiceResponseModelV2 getV2Model (ElasticQueryServiceResponseModel responseModel) {

        ElasticQueryServiceResponseModelV2 responseModelV2 = ElasticQueryServiceResponseModelV2.builder ()
                .id (Long.valueOf (responseModel.getId ()))
                .userId (responseModel.getUserId ())
                .text (responseModel.getText ())
                .createdAt (responseModel.getCreatedAt ())
                .build ();

        responseModelV2.add (responseModel.getLinks ());

        return responseModelV2;
    }

}
