package ru.zolo.schedule.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component("bye")
public class ByeJob extends JobBase {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Bye, world!");
    }
}

