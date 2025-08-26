package ru.zolo.schedule;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public class JobSchedulerHelper {

    public static JobDetail buildJobDetail(Class<? extends Job> jobClass, String name) {
        return JobBuilder.newJob(jobClass)
                .withIdentity(name + "_job")
                .storeDurably()
                .build();
    }

    public static Trigger buildCronTrigger(JobDetail jobDetail, String name, String cronExpression) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(name + "_trigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();
    }
}
