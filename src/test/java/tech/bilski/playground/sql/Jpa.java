package tech.bilski.playground.sql;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class Jpa {

    @Autowired
    private JpaEntityDao dao;

    @Container
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14.2")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("postgres")
            .withInitScript("schema.sql");

    @DynamicPropertySource
    static void dbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

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
//        entity.getList().add("third");    // doesn't work, list is immutable
        var newList = new ArrayList<>(saved.getList());
        newList.add("third");
        saved.setList(newList);
        dao.save(saved);

        var read = dao.findById(saved.getId());
        assertThat(read.getList())
                .hasSize(3)
                .contains("first", "second", "third");
    }
}
