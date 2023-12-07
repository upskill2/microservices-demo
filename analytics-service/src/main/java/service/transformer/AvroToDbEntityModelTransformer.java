package service.transformer;

import com.microservices.demo.kafka.avro.model.TwitterAnalyticsAvroModel;
import org.springframework.stereotype.Component;
import org.springframework.util.IdGenerator;
import service.dataaccess.entity.AnalyticsEntity;

import javax.persistence.Column;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AvroToDbEntityModelTransformer {

    private final IdGenerator idGenerator;

    public AvroToDbEntityModelTransformer (IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public List<AnalyticsEntity> getEntityModel (List<TwitterAnalyticsAvroModel> avroModels) {

        return avroModels.stream ()
                .map (avroModel -> new AnalyticsEntity (
                        idGenerator.generateId (),
                        avroModel.getWord (),
                        avroModel.getWordCount (),
                        LocalDateTime.ofInstant (Instant.ofEpochSecond (avroModel.getCreatedAt ()), java.time.ZoneId.of ("UTC")
                        ))).collect (Collectors.toList ());

    }
}
