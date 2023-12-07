package service.dataaccess.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table (name = "twitter_analytics")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsEntity implements BaseEntity<UUID> {

    @Id
    @NotNull
    @Column (name = "id", columnDefinition = "uuid")
    private UUID id;

    @NotNull
    @Column (name = "word")
    private String word;

    @NotNull
    @Column (name = "word_count")
    private Long wordCount;

    @NotNull
    @Column (name = "record_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime recordDate;

   @Override
    public int hashCode () {
        return Objects.hash (id);
    }
}
