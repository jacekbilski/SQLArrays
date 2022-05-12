package tech.bilski.playground.sql;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Table("entity_with_list")
public class EntityWithList {
    @Id
    Long id;

    List<String> list;
}
