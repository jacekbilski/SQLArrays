package tech.bilski.playground.sql;

import org.springframework.data.repository.CrudRepository;

public interface EntityWithListJdbcRepository extends CrudRepository<EntityWithList, Long> {
}
