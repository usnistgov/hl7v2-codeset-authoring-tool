package gov.nist.hit.hl7.codesetauthoringtool.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Pattern;

//package gov.nist.hit.hl7.codesetauthoringtool.configuration;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class DataSourceConfig {
//
//    private final  Environment env;
//    public DataSourceConfig(Environment env){
//        this.env = env;
//    }
//
//
//    @Bean
//    public DataSource dataSource() {
//        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(env.getProperty("driverClassName"));
//        dataSource.setUrl(env.getProperty("url"));
//        dataSource.setUsername(env.getProperty("user"));
//        dataSource.setPassword(env.getProperty("password"));
//        return dataSource;
//    }
//}
@Configuration
public class DataSourceConfig {

    private final Environment env;

    public DataSourceConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
//        dataSource.setDriverClassName(env.getProperty("driverClassName", "org.sqlite.JDBC"));
        dataSource.setJdbcUrl(env.getProperty("url", "jdbc:postgresql://localhost:5432/postgres"));
        dataSource.setUsername(env.getProperty("username", ""));
        dataSource.setPassword(env.getProperty("password", ""));

//        // SQLite-specific HikariCP properties
//        dataSource.addDataSourceProperty("journal_mode", "WAL");
//        dataSource.addDataSourceProperty("synchronous", "NORMAL");
//        dataSource.addDataSourceProperty("busy_timeout", "5000");
//        dataSource.addDataSourceProperty("locking_mode", "IMMEDIATE");
//
//        // Connection pool settings
//        dataSource.setMaximumPoolSize(1);  // Single connection for writes
//        dataSource.setMinimumIdle(0);     // No idle connections
//        dataSource.setIdleTimeout(600000); // 10 minutes
//        dataSource.setMaxLifetime(1800000); // 30 minutes

        return dataSource;
    }


}
