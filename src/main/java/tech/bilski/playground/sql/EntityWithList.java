package tech.bilski.playground.sql;

import org.springframework.data.annotation.Id;

import java.util.List;

public class EntityWithList {
    @Id
    Long id;

    List<String> list;
}
