package com.microservices.demo.kafka.streams.service.api;

import com.microservices.demo.kafka.streams.service.model.KafkaStreamResponseModel;
import com.microservices.demo.kafka.streams.service.runner.StreamsRunner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (value = "/", produces = "application/vmd.api.v1+json")
@Slf4j
public class KafkaStreamController {

    public KafkaStreamController (StreamsRunner<String, Long> streamsRunner) {
        this.streamsRunner = streamsRunner;
    }

    private final StreamsRunner<String, Long> streamsRunner;


    @GetMapping ("get-word-count-by-word/{word}")
    @Operation (summary = "Get word count by word")
    @ApiResponses (value = {
            @ApiResponse (responseCode = "200", description = "Successful response", content = {
                    @Content (mediaType = "application/vnd.api.v1+json",
                            schema = @Schema (implementation = KafkaStreamResponseModel.class))
            }),
            @ApiResponse (responseCode = "500", description = "Internal server error"),
            @ApiResponse (responseCode = "400", description = "Not Found")
    })
    public ResponseEntity<KafkaStreamResponseModel> getWordCount (@PathVariable String word) {
        log.info ("Received request for word count of {}", word);
        Long wordCount = streamsRunner.getValueByKey (word);
        return ResponseEntity.ok (KafkaStreamResponseModel
                .builder ()
                .word (word)
                .wordCount (wordCount)
                .build ());
    }

}