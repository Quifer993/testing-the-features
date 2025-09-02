package ru.zolo.schedule;

import lombok.Getter;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JobSchedulerHelper {
    @Value(value = "${app.name:crm}")
    @Getter
    public String appName;

    public JobDetail buildJobDetail(Class<? extends Job> jobClass, String name) {
        return JobBuilder.newJob(jobClass)
                .withIdentity(name, appName)
                .storeDurably()
                .requestRecovery()
                .build();
    }

    public Trigger buildCronTrigger(JobDetail jobDetail, String name, CronScheduleBuilder cronExpression) {
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(name, appName)
                .withSchedule(cronExpression)
                .build();
        return trigger;
    }
}
