package ru.zolo.schedule.jobs;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import ru.zolo.utils.annotation.quartz.ScheduledProperties;

import java.time.Instant;

@Component
@DisallowConcurrentExecution
@ScheduledProperties(name = "hello")
public class HelloJob extends JobBase {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Hello, world! TIME: " + Instant.now());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Bye, world! TIME: " + Instant.now());

    }
}
