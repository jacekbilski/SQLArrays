package tech.bilski.playground.sql;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@SpringBootTest
public class Playground {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Test
    void x() {
        System.out.println("x");
    }
}
