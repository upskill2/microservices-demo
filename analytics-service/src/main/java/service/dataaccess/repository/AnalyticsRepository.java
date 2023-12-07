package service.dataaccess.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import service.dataaccess.entity.AnalyticsEntity;

import java.util.List;
import java.util.UUID;

public interface AnalyticsRepository extends JpaRepository<AnalyticsEntity, UUID>,
        AnalyticsCustomRepository<AnalyticsEntity, UUID> {

    @Query (value = "SELECT e FROM AnalyticsEntity e WHERE e.word=:word order by e.recordDate desc")
    List<AnalyticsEntity> getAnalyticsEntitiesByWord (@Param ("word") String word, Pageable pageable);
}
