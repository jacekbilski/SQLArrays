package tech.bilski.playground.sql;

import org.springframework.data.repository.CrudRepository;

public interface EntityWithArrayJdbcRepository extends CrudRepository<EntityWithArray, Long> {
}
