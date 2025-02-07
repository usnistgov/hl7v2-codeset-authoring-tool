package gov.nist.hit.hl7.codesetauthoringtool.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    private final Environment env;

    public DataSourceConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(env.getProperty("url", "jdbc:postgresql://localhost:5432/postgres"));
        dataSource.setUsername(env.getProperty("username", ""));
        dataSource.setPassword(env.getProperty("password", ""));

        return dataSource;
    }


}
