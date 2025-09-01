package ru.zolo.schedule.jobs;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.zolo.service.auto.SampleJobService;
import ru.zolo.utils.annotation.quartz.ScheduledProperties;

@Component
@DisallowConcurrentExecution
@ScheduledProperties(name = "sample")
public class SampleJob extends JobBase {
    @Autowired
    SampleJobService sampleJobService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        super.execute(context);
        sampleJobService.executeSampleJob();

    }
}
