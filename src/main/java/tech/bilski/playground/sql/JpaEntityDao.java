package tech.bilski.playground.sql;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class JpaEntityDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public JpaEntity save(JpaEntity entity) {
        return em.merge(entity);
    }

    public JpaEntity findById(Long id) {
        return em.find(JpaEntity.class, id);
    }
}
