package service.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.business.impl.AnalyticsService;
import service.model.AnalyticsResponseModel;

import javax.validation.constraints.NotEmpty;
import java.util.Optional;

@RestController
@RequestMapping (value = "/", produces = "application/vnd.api.v1+json")
@Slf4j
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    public AnalyticsController (AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }


    @GetMapping ("/get-word-count-by-word/{word}")
    @Operation (summary = "Get word count by word")
    @ApiResponses (value = {
            @ApiResponse (responseCode = "200", description = "Word count by word retrieved",
                    content = {@Content (mediaType = "application/vnd.api.v1+json",
                            schema = @Schema (implementation = AnalyticsResponseModel.class))}),
            @ApiResponse (responseCode = "400", description = "Invalid input"),
            @ApiResponse (responseCode = "404", description = "Word count by word not found")
    })
    public @ResponseBody
    ResponseEntity<AnalyticsResponseModel> getWordCountByWord (@PathVariable @NotEmpty String word) {

        Optional<AnalyticsResponseModel> analyticsResponseModel = analyticsService.getWordAnalytics (word);
        if (analyticsResponseModel.isPresent ()) {
            log.info ("Analytics response model: {}", analyticsResponseModel.get ());
            return ResponseEntity.ok (analyticsResponseModel.get ());
        }
        return ResponseEntity.ok (AnalyticsResponseModel.builder ().build ());
    }

}
