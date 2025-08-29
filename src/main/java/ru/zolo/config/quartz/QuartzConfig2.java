package ru.zolo.config.quartz;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.quartz.TriggerListener;
import org.quartz.utils.ConnectionProvider;
import org.quartz.utils.DBConnectionManager;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.zolo.config.quartz.listiner.QuartzTriggerListener;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
@RequiredArgsConstructor
public class QuartzConfig2 {
    @Bean
    public TriggerListener quartzTriggerListenerCreate() {
        return new QuartzTriggerListener();
    }

    @Bean
    @ConfigurationProperties("datasource-quartz")
    DataSourceProperties quartzDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("quartzDataSource")
    @ConfigurationProperties("datasource-quartz.configuration")
    @QuartzDataSource
    public DataSource quartzDataSource() {
        return quartzDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @PostConstruct
    public void registerQuartzConnectionProvider() {
        DBConnectionManager.getInstance().addConnectionProvider("quartzDataSource", new ConnectionProvider() {
            @Override
            public Connection getConnection() throws SQLException {
                return quartzDataSource().getConnection();
            }

            @Override
            public void shutdown() {
            }

            @Override
            public void initialize() {
            }
        });
    }
}
