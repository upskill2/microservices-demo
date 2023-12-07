package service.business.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import service.dataaccess.repository.AnalyticsRepository;
import service.model.AnalyticsResponseModel;
import service.transformer.EntityToResponseModelTransformer;

import java.util.Optional;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final AnalyticsRepository analyticsRepository;
    private final EntityToResponseModelTransformer entityToResponseModelTransformer;

    public AnalyticsServiceImpl (AnalyticsRepository analyticsRepository, EntityToResponseModelTransformer entityToResponseModelTransformer) {
        this.analyticsRepository = analyticsRepository;
        this.entityToResponseModelTransformer = entityToResponseModelTransformer;
    }

    @Override
    public Optional<AnalyticsResponseModel> getWordAnalytics (final String word) {
        return entityToResponseModelTransformer.getResponseModel (
                analyticsRepository.getAnalyticsEntitiesByWord (word, PageRequest.of (0, 1))
                        .stream ().findFirst ().orElse (null));
    }
}
