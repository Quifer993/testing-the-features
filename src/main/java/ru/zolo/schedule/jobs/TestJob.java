package ru.zolo.schedule.jobs;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Component("test2")
@DisallowConcurrentExecution
public class TestJob extends JobBase {
    static LocalDateTime start = null;

    static AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        if (start == null) {
            start = LocalDateTime.now();
        }
        int i = counter.incrementAndGet();

        UUID uuid = UUID.randomUUID();
        System.out.println(uuid + " start -" + ((double) Duration.between(LocalDateTime.now(), start).toMillis() / 1000d) + " seconds");


        try {
            if (i < 7) {
//            if(new Random().nextBoolean()) {
                Thread.sleep(8000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(uuid + " finished - " + ((double) Duration.between(LocalDateTime.now(), start).toMillis() / 1000d) + " seconds");
    }
}

