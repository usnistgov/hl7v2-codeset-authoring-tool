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

//    @Bean
//    public DataSource dataSource() {
//        HikariDataSource dataSource = new HikariDataSource();
//        dataSource.setJdbcUrl(env.getProperty("spring.datasource.url", ""));
//        dataSource.setUsername(env.getProperty("db-username", ""));
//        dataSource.setPassword(env.getProperty("db-password", ""));
//
//        return dataSource;
//    }


}
