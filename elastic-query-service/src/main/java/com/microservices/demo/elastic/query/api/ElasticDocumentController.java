package com.microservices.demo.elastic.query.api;

import com.microservices.demo.elastic.query.model.ElasticQueryServiceRequestModel;
import com.microservices.demo.elastic.query.model.ElasticQueryServiceResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping (value = "/documents")
@Slf4j
public class ElasticDocumentController {

    @GetMapping ("/")
    public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>> getAllDocuments () {
        List<ElasticQueryServiceResponseModel> response = new ArrayList<> ();
        log.info ("Elastic returned {} of socunments", response.size ());
        return ResponseEntity.ok (response);
    }


    public @ResponseBody ResponseEntity<ElasticQueryServiceResponseModel>
    getDocumentById (@PathVariable String id) {
        ElasticQueryServiceResponseModel response = ElasticQueryServiceResponseModel.
                builder ()
                .id (id).
                build ();
        log.info ("Elastic returned document with id {}", id);
        return ResponseEntity.ok (response);
    }


    @PostMapping ("/get-document-by-text")
    public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>> getDocumentsByText (
            @RequestBody ElasticQueryServiceRequestModel request) {
        List<ElasticQueryServiceResponseModel> response = new ArrayList<> ();
        ElasticQueryServiceResponseModel responseModel = ElasticQueryServiceResponseModel.
                builder ()
                .text (request.getText ())
                .build ();
        response.add (responseModel);

        log.info ("Elastic returned {} of documents", response.size ());
        return ResponseEntity.ok (response);
    }
}
