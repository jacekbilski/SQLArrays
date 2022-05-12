package tech.bilski.playground.sql;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Disabled
public class Jpa {

    @Autowired
    private JpaEntityDao dao;

    @Test
    void save() {
        var entity = new JpaEntity();
        entity.setList(List.of("first", "second"));
        var saved = dao.save(entity);
        assertThat(saved).isNotNull();
    }

    @Test
    void get() {
        var entity = new JpaEntity();
        entity.setList(List.of("first", "second"));
        var saved = dao.save(entity);

        var read = dao.findById(saved.getId());
        assertThat(read.getList())
                .hasSize(2)
                .contains("first", "second");
    }

    @Test
    void update() {
        var entity = new JpaEntity();
        entity.setList(List.of("first", "second"));
        var saved = dao.save(entity);
        entity.getList().add("third");
        dao.save(saved);

        var read = dao.findById(saved.getId());
        assertThat(read.getList())
                .hasSize(3)
                .contains("first", "second", "third");
    }
}
