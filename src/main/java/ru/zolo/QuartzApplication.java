package ru.zolo;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.zolo.properties.ScheduleProperties;

@SpringBootApplication
public class QuartzApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(QuartzApplication.class, args);
        run.getBean(ScheduleProperties.class);
    }

}