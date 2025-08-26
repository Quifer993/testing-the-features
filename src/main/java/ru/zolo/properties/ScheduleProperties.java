package ru.zolo.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "schedule")
@Getter
@Setter
public class ScheduleProperties {

    private HashSet<JobConfig> jobs;

    @Getter
    @Setter
    public static class JobConfig {
        private String name;
        private String cron;
        private String className;
    }
}
