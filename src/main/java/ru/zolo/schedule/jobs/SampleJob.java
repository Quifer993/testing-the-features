package ru.zolo.schedule.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.zolo.service.auto.SampleJobService;

@Component
public class SampleJob implements Job {

    @Autowired
    private SampleJobService jobService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        jobService.executeSampleJob();
    }
}
