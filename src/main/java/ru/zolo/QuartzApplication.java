package ru.zolo;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class QuartzApplication {

    @SneakyThrows
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(QuartzApplication.class, args);
        System.out.printf("Quartz application started with args: %s%n", Arrays.toString(args));
        Thread.sleep(22000);

    }

}