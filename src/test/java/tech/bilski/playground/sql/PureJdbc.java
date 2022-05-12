package tech.bilski.playground.sql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Array;
import java.sql.SQLException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("SqlWithoutWhere")
@SpringBootTest
@Testcontainers
public class PureJdbc {

    private static final Logger logger = LoggerFactory.getLogger(PureJdbc.class);

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

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

    @BeforeEach
    void cleanup() {
        jdbcTemplate.update("TRUNCATE TABLE PURE_JDBC", new MapSqlParameterSource());
    }

    @Test
    void insert() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        var count = jdbcTemplate.update("INSERT INTO PURE_JDBC (arr) VALUES ('{\"first\", \"second\"}')", new MapSqlParameterSource(), keyHolder);
        assertThat(count).isEqualTo(1);
        var id = getId(keyHolder);
        logger.info("Actual key: '{}'", id);
    }

    @Test
    void insert_usingParam() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final var items = new String[]{"first", "second"};
        final var params = new MapSqlParameterSource("items", items);
        var count = jdbcTemplate.update("INSERT INTO PURE_JDBC (arr) VALUES (:items)", params, keyHolder);
        assertThat(count).isEqualTo(1);
        var id = getId(keyHolder);
        logger.info("Actual key: '{}'", id);
    }

    @Test
    void append() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        var count = jdbcTemplate.update("INSERT INTO PURE_JDBC (arr) VALUES ('{\"first\", \"second\"}')", new MapSqlParameterSource(), keyHolder);
        assertThat(count).isEqualTo(1);
        var id = getId(keyHolder);

        final var newItems = new String[]{"third", "fourth"};
        final var params = new MapSqlParameterSource();
        params.addValue("items", newItems);
        params.addValue("id", id);
        count = jdbcTemplate.update("UPDATE PURE_JDBC SET arr = arr || :items WHERE id = :id", params);
        assertThat(count).isEqualTo(1);

        count = jdbcTemplate.queryForObject("SELECT cardinality(arr) FROM PURE_JDBC WHERE id = :id", new MapSqlParameterSource("id", id), Integer.class);
        assertThat(count).isEqualTo(4);
    }

    @Test
    void remove() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        var count = jdbcTemplate.update("INSERT INTO PURE_JDBC (arr) VALUES ('{\"first\", \"second\"}')", new MapSqlParameterSource(), keyHolder);
        assertThat(count).isEqualTo(1);
        var id = getId(keyHolder);

        final var itemToRemove = "first";
        final var params = new MapSqlParameterSource();
        params.addValue("item", itemToRemove);
        params.addValue("id", id);
        count = jdbcTemplate.update("UPDATE PURE_JDBC SET arr = array_remove(arr, :item) WHERE id = :id", params);
        assertThat(count).isEqualTo(1);

        count = jdbcTemplate.queryForObject("SELECT cardinality(arr) FROM PURE_JDBC WHERE id = :id", new MapSqlParameterSource("id", id), Integer.class);
        assertThat(count).isEqualTo(1);
    }

    @Test
    void get() throws SQLException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        var count = jdbcTemplate.update("INSERT INTO PURE_JDBC (arr) VALUES ('{\"first\", \"second\"}')", new MapSqlParameterSource(), keyHolder);
        assertThat(count).isEqualTo(1);
        var id = getId(keyHolder);

        var sqlArray = jdbcTemplate.queryForObject("SELECT arr FROM PURE_JDBC WHERE id = :id", new MapSqlParameterSource("id", id), Array.class);
        var array = ((String[]) sqlArray.getArray());
        assertThat(array)
                .hasSize(2)
                .contains("first", "second");
    }

    private Number getId(KeyHolder keyHolder) {
        var keyList = keyHolder.getKeyList();
        var entry = keyList.get(0);
        return (Number) entry.entrySet()
                             .stream()
                             .filter(e -> e.getKey().equals("id"))
                             .map(Map.Entry::getValue)
                             .findFirst()
                             .get();
    }
}
