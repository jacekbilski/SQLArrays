package tech.bilski.playground.sql;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableJpaRepositories
@EnableJdbcRepositories(basePackageClasses = EntityWithArrayJdbcRepository.class)
@ComponentScan(basePackageClasses = AppConfig.class)
public class AppConfig {

}
