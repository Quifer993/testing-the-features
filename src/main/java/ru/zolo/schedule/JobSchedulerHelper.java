package ru.zolo.schedule;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

public class JobSchedulerHelper {

    public static JobDetail buildJobDetail(Class<? extends Job> jobClass, String name) {
        return JobBuilder.newJob(jobClass)
                .withIdentity(name + "_job")
                .storeDurably()
                .build();
    }

    public static Trigger buildCronTrigger(JobDetail jobDetail, String name, CronScheduleBuilder cronExpression) {
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(name + "_trigger")
                .withSchedule(cronExpression)
                .build();
        return trigger;
    }

//    @Bean
//    public static Triiger cronTrigger(JobDetail jobDetail, String name, String cronExpression) {
//        CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
//
//        trigger.setBeanName(jobDetail.getKey().getName());
//        trigger.setJobDetail(jobDetail);
//        trigger.setBeanName(name + "_trigger");
//        trigger.setCronExpression(cronExpression);
//        trigger.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
////        trigger.
//        return trigger;
//    }
}
