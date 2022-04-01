package tech.bilski.playground.sql;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("SqlWithoutWhere")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Playground {

    private static final Logger logger = LoggerFactory.getLogger(Playground.class);

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeAll
    void setup() {
        jdbcTemplate.getJdbcOperations().execute("DROP TABLE IF EXISTS XYZ");
        jdbcTemplate.getJdbcOperations().execute("CREATE TABLE XYZ (id INTEGER PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY, arr TEXT ARRAY[5])");
    }

    @BeforeEach
    void cleanup() {
        jdbcTemplate.update("DELETE FROM XYZ", new MapSqlParameterSource());
    }

    @Test
    void pureJdbc_insert() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        var count = jdbcTemplate.update("INSERT INTO XYZ (arr) VALUES ('{\"first\", \"second\"}')", new MapSqlParameterSource(), keyHolder);
        assertThat(count).isEqualTo(1);
        var id = getId(keyHolder);
        logger.info("Actual key: '{}'", id);
    }

    @Test
    void pureJdbc_insert_usingParam() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final var items = new String[]{"first", "second"};
        final var params = new MapSqlParameterSource("items", items);
        var count = jdbcTemplate.update("INSERT INTO XYZ (arr) VALUES (:items)", params, keyHolder);
        assertThat(count).isEqualTo(1);
        var id = getId(keyHolder);
        logger.info("Actual key: '{}'", id);
    }

    @Test
    void pureJdbc_append() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        var count = jdbcTemplate.update("INSERT INTO XYZ (arr) VALUES ('{\"first\", \"second\"}')", new MapSqlParameterSource(), keyHolder);
        assertThat(count).isEqualTo(1);
        var id = getId(keyHolder);

        final var newItems = new String[]{"third", "fourth"};
        final var params = new MapSqlParameterSource();
        params.addValue("items", newItems);
        params.addValue("id", id);
        count = jdbcTemplate.update("UPDATE XYZ SET arr = arr || :items WHERE id = :id", params);
        assertThat(count).isEqualTo(1);

        count = jdbcTemplate.queryForObject("SELECT cardinality(arr) FROM XYZ WHERE id = :id", new MapSqlParameterSource("id", id), Integer.class);
        assertThat(count).isEqualTo(4);
    }

//    @Test
//    void pureJdbc_remove() {
//        assertThat(result).;
//    }

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
