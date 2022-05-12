package tech.bilski.playground.sql;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JdbcRepositoryWithList {

    @Autowired
    private EntityWithListJdbcRepository repository;

    @Test
    void save() {
        var entity = new EntityWithList();
        entity.list = List.of("first", "second");
        var saved = repository.save(entity);
        assertThat(saved).isNotNull();
    }

    @Test
    void get() {
        var entity = new EntityWithList();
        entity.list = List.of("first", "second");
        var saved = repository.save(entity);

        var read = repository.findById(saved.id);
        assertThat(read).isPresent();
        var readEntity = read.get();
        assertThat(readEntity.list)
                .hasSize(2)
                .contains("first", "second");
    }

    @Test
    void update() {
        var entity = new EntityWithList();
        entity.list = List.of("first", "second");
        var saved = repository.save(entity);
//        saved.arr.add("third");   // doesn't work, list is immutable
        var newList = new ArrayList<>(saved.list);
        newList.add("third");
        saved.list = newList;
        repository.save(saved);

        var read = repository.findById(saved.id);
        assertThat(read).isPresent();
        var readEntity = read.get();
        assertThat(readEntity.list)
                .hasSize(3)
                .contains("first", "second", "third");
    }
}
