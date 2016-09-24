package dev.gsitgithub.webapp.config.database;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
@Profile("!localhost")
public class MySqlDatabaseConfig {

    @Value("${database.url}")
    private String url;
    @Value("${database.driver}")
    private String driver;
    @Value("${database.username}")
    private String username;
    @Value("${database.password}")
    private String password;

    // Data Source
    @Bean
    public DataSource dataSource() throws SQLException {
        createDatabaseIfItDoesNotExist();
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driver);
        return dataSource;
    }

    // Jpa Vendor Adapter
    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.MYSQL);
        jpaVendorAdapter.setShowSql(false);
        jpaVendorAdapter.setGenerateDdl(false);
        return jpaVendorAdapter;
    }

    private void createDatabaseIfItDoesNotExist() throws SQLException {
        String databaseServerUrl = removeDatabaseName(url);
        String databaseName = parseDatabaseName(url);
        Connection connection = DriverManager.getConnection(databaseServerUrl, username, password);
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + databaseName + "`");
    }

    private String removeDatabaseName(String url) {
        return url.replaceAll("/[^/]*$", "");
    }

    private String parseDatabaseName(String url) {
        return url.replaceAll(".*/", "");
    }
}
