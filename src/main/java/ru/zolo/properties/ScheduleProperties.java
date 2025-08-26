package ru.zolo.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "schedule")
@Getter
@Setter
public class ScheduleProperties {
    private HashMap<String, JobConfig> jobs;

    @Getter
    @Setter
    public static class JobConfig {
        private String cron;
    }
}
