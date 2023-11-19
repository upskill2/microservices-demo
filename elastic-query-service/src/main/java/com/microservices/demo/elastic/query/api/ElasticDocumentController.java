package com.microservices.demo.elastic.query.api;

import com.microservices.demo.elastic.query.model.ElasticQueryServiceRequestModel;
import com.microservices.demo.elastic.query.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.query.model.ElasticQueryServiceResponseModelV2;
import com.microservices.demo.elastic.query.service.ElasticQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping (value = "/documents", produces = "application/vnd.api.v1+json")
@Slf4j
public class ElasticDocumentController {

    private final ElasticQueryService queryService;

    public ElasticDocumentController (final ElasticQueryService queryService) {
        this.queryService = queryService;
    }

    @Value ("${server.port}")
    public String port;

    @Operation (summary = "Get all documents from elasticsearch")
    @ApiResponses (value = { @ApiResponse (responseCode = "200", description = "Successful response", content = {
            @Content (mediaType = "application/vnd.api.v1+json",
                    schema = @Schema (implementation = ElasticQueryServiceResponseModel.class)),
    }),
    @ApiResponse (responseCode = "500", description = "Internal server error"),
    @ApiResponse (responseCode = "400", description = "Not Found")
    })
    @GetMapping ("/")
    public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>> getAllDocuments () {
        List<ElasticQueryServiceResponseModel> response = queryService.getAllDocuments ();
        log.info ("Elastic returned {} of documents", response.size ());
        List<ElasticQueryServiceResponseModel> responseShort = response.subList (0, 100);
        return ResponseEntity.ok (responseShort);
    }


    @Operation (summary = "Get documents from elasticsearch by id")
    @ApiResponses (value = { @ApiResponse (responseCode = "200", description = "Successful response", content = {
            @Content (mediaType = "application/vnd.api.v1+json",
                    schema = @Schema (implementation = ElasticQueryServiceResponseModel.class)),
    }),
            @ApiResponse (responseCode = "500", description = "Internal server error"),
            @ApiResponse (responseCode = "400", description = "Not Found")
    })
    @GetMapping ("/{id}")
    public @ResponseBody ResponseEntity<ElasticQueryServiceResponseModel>
    getDocumentById (@PathVariable @NotEmpty String id) {
        ElasticQueryServiceResponseModel response = queryService.getDocumentById (id);
        log.info ("Elastic returned document with id {}", id);
        return ResponseEntity.ok (response);
    }

    @Operation (summary = "Get all documents from elasticsearch by id v2")
    @ApiResponses (value = { @ApiResponse (responseCode = "200", description = "Successful response", content = {
            @Content (mediaType = "application/vnd.api.v2+json",
                    schema = @Schema (implementation = ElasticQueryServiceResponseModelV2.class)),
    }),
            @ApiResponse (responseCode = "500", description = "Internal server error"),
            @ApiResponse (responseCode = "400", description = "Not Found")
    })
    @GetMapping (value = "/{id}", produces = "application/vnd.api.v2+json")
    public @ResponseBody ResponseEntity<ElasticQueryServiceResponseModelV2>
    getDocumentByIdV2 (@PathVariable @NotEmpty String id) {
        ElasticQueryServiceResponseModel response = queryService.getDocumentById (id);
        log.info ("Elastic returned document with id {}", id);
        return ResponseEntity.ok (getTransformedResponse (response));
    }



    @Operation (summary = "Get all documents from elasticsearch by text")
    @ApiResponses (value = { @ApiResponse (responseCode = "200", description = "Successful response", content = {
            @Content (mediaType = "application/vnd.api.v1+json",
                    schema = @Schema (implementation = ElasticQueryServiceResponseModel.class)),
    }),
            @ApiResponse (responseCode = "500", description = "Internal server error"),
            @ApiResponse (responseCode = "400", description = "Not Found")
    })
    @PostMapping ("/get-document-by-text")
    public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>> getDocumentsByText (
            @RequestBody @Valid ElasticQueryServiceRequestModel request) {
        List<ElasticQueryServiceResponseModel> response = queryService.
                getDocumentByText (request.getText ())
                        .stream ()
                                .limit (100)
                                        .collect(Collectors.toList());

        log.info ("Elastic returned {} of documents on port {}", response.size (), port);
        return ResponseEntity.ok (response);
    }

    private ElasticQueryServiceResponseModelV2 getTransformedResponse (ElasticQueryServiceResponseModel response) {
        final ElasticQueryServiceResponseModelV2 responseV2 = ElasticQueryServiceResponseModelV2.builder ()
                .id (Long.valueOf (response.getId ()))
                .userId (response.getUserId ())
                .text (response.getText ())
                .textV2 ("Version 2 text")
                .build ();
        responseV2.add (response.getLinks ());

        return responseV2;
    }

}
