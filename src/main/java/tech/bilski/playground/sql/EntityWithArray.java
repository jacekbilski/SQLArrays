package tech.bilski.playground.sql;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("entity_with_array")
public class EntityWithArray {
    @Id
    Long id;

    String[] arr;
}
