package tech.bilski.playground.sql;

import org.springframework.data.annotation.Id;

public class EntityWithArray {
    @Id
    Long id;

    String[] arr;
}
