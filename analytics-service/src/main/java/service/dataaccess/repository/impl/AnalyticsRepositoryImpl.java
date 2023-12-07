package service.dataaccess.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import service.dataaccess.entity.AnalyticsEntity;
import service.dataaccess.entity.BaseEntity;
import service.dataaccess.repository.AnalyticsCustomRepository;
import service.dataaccess.repository.AnalyticsRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.UUID;

@Repository
@Slf4j
public class AnalyticsRepositoryImpl<T extends BaseEntity<PK>, PK> implements AnalyticsCustomRepository<T, PK> {
    @PersistenceContext
    protected EntityManager em;

    @Value ("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    protected int batchSize;

    @Override
    @Transactional
    public <S extends T> PK persist (final S entity) {
        this.em.persist (entity);
        return entity.getId ();
    }

    @Override
    public <S extends T> void batchPersist (final Collection<S> entities) {

        if (entities.isEmpty ()) {
            log.info ("No entities to persist");
            return;
        }
        int batchCount = 0;
        for (S entity : entities) {
            this.em.persist (entity);
            batchCount++;
            if (batchCount % batchSize == 0) {
                this.em.flush ();
                this.em.clear ();
            }
        }
        if (batchCount % batchSize != 0) {
            this.em.flush ();
            this.em.clear ();
        }
    }

    @Override
    public <S extends T> S merge (final S entity) {
        return this.em.merge (entity);
    }

    @Override
    public <S extends T> void batchMerge (final Collection<S> entities) {

        if (entities.isEmpty ()) {
            log.info ("No entities to persist");
            return;
        }
        int batchCount = 0;
        for (S entity : entities) {
            this.em.merge (entity);
            batchCount++;
            if (batchCount % batchSize == 0) {
                this.em.flush ();
                this.em.clear ();
            }
        }
        if (batchCount % batchSize != 0) {
            this.em.flush ();
            this.em.clear ();
        }

    }

    @Override
    public void clear () {
        this.em.clear ();

    }
}
