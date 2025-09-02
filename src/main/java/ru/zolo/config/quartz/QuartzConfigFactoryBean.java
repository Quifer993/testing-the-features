package ru.zolo.config.quartz;

import lombok.RequiredArgsConstructor;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import ru.zolo.properties.ScheduleProperties;
import ru.zolo.schedule.JobSchedulerHelper;
import ru.zolo.schedule.jobs.JobBase;
import ru.zolo.utils.annotation.quartz.ScheduledProperties;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class QuartzConfigFactoryBean {
    private final ScheduleProperties scheduleProperties;
    private final JobSchedulerHelper jobSchedulerHelper;

    @Bean
    public SchedulerFactoryBeanCustomizer schedulerFactoryBeanCustomizer(List<Job> jobs) {
        return factory -> {
            List<JobDetail> jobDetails = new ArrayList<>();
            List<Trigger> triggers = new ArrayList<>();

            for (Job job2 : jobs) {
                Class<? extends Job> clazz = job2.getClass();
                ScheduledProperties annotation = clazz.getAnnotation(ScheduledProperties.class);
                if (JobBase.class.isAssignableFrom(clazz) && annotation != null) {
                    String jobName = annotation.name();
                    JobBase job = (JobBase) job2;

                    Optional.ofNullable(scheduleProperties.getJobs().get(jobName)).ifPresent(jobConfig -> {
                        String cron = jobConfig.getCron();
                        if (cron != null && CronExpression.isValidExpression(cron)) {
                            CronScheduleBuilder cronExpression = CronScheduleBuilder.cronSchedule(cron)
//                                    .withMisfireHandlingInstructionFireAndProceed();
//                                    .withMisfireHandlingInstructionIgnoreMisfires();
                                    .withMisfireHandlingInstructionDoNothing();

                            Class<? extends Job> jobClass = job.getClass();

                            JobDetail jobDetail = jobSchedulerHelper.buildJobDetail(jobClass, jobName);
                            Trigger trigger = jobSchedulerHelper.buildCronTrigger(jobDetail, jobName, cronExpression);

                            jobDetails.add(jobDetail);
                            triggers.add(trigger);
                        }
                    });
                }
            }


            factory.setJobDetails(jobDetails.toArray(new JobDetail[0]));
            factory.setTriggers(triggers.toArray(new Trigger[0]));
            setProperty(factory, "org.quartz.threadPool.threadCount", Integer.valueOf(triggers.size() + 1/*кол-во заранее поднятных мб*/).toString());
        };
    }

    private static void setProperty(SchedulerFactoryBean factory, String name, String value) {
        try {
            Field fieldQuartzProperties = SchedulerFactoryBean.class.getDeclaredField("quartzProperties");
            fieldQuartzProperties.setAccessible(true);
            Properties prop = (Properties) fieldQuartzProperties.get(factory);
            prop.put(name, value);
            fieldQuartzProperties.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}