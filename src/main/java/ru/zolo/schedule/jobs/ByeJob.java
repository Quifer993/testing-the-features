package ru.zolo.schedule.jobs;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import ru.zolo.utils.annotation.quartz.ScheduledProperties;

import java.time.Instant;

@Component
@DisallowConcurrentExecution
@ScheduledProperties(name = "bye")
public class ByeJob extends JobBase {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Bye, world! TIME: " + Instant.now());
    }
}

