package tech.bilski.playground.sql;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AppConfig.class)
public class JdbcRepositoryWithArray {

    @Autowired
    private EntityWithArrayJdbcRepository repository;

    @Test
    void save() {
        var entity = new EntityWithArray();
        entity.arr = new String[]{"first", "second"};
        var saved = repository.save(entity);
        assertThat(saved).isNotNull();
    }

    @Test
    void get() {
        var entity = new EntityWithArray();
        entity.arr = new String[]{"first", "second"};
        var saved = repository.save(entity);

        var read = repository.findById(saved.id);
        assertThat(read).isPresent();
        var readEntity = read.get();
        assertThat(readEntity.arr)
                .hasSize(2)
                .contains("first", "second");
    }

    @Test
    void update() {
        var entity = new EntityWithArray();
        entity.arr = new String[]{"first", "second"};
        var saved = repository.save(entity);
        saved.arr[0] = "third";
        repository.save(saved);

        var read = repository.findById(saved.id);
        assertThat(read).isPresent();
        var readEntity = read.get();
        assertThat(readEntity.arr)
                .hasSize(2)
                .contains("second", "third");
    }
}
