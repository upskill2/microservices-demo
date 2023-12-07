package service.transformer;

import org.springframework.stereotype.Component;
import service.dataaccess.entity.AnalyticsEntity;
import service.model.AnalyticsResponseModel;

import java.util.Optional;

@Component
public class EntityToResponseModelTransformer {

    public Optional<AnalyticsResponseModel> getResponseModel(AnalyticsEntity twitterAnalyticsEntity) {
        if (twitterAnalyticsEntity == null)
            return Optional.empty();
        return Optional.ofNullable(AnalyticsResponseModel
                .builder()
                .id(twitterAnalyticsEntity.getId())
                .word(twitterAnalyticsEntity.getWord())
                .wordCount(twitterAnalyticsEntity.getWordCount())
                .build());
    }
}
