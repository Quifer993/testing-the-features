package ru.zolo.config;

import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import ru.zolo.properties.ScheduleProperties;
import ru.zolo.schedule.JobSchedulerHelper;
import ru.zolo.schedule.jobs.ByeJob;
import ru.zolo.schedule.jobs.HelloJob;
import ru.zolo.schedule.jobs.JobBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@Configuration
//@RequiredArgsConstructor
//public class QuartzConfig {
//
//    private final ScheduleProperties scheduleProperties;
//
//    @Bean

/// /    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//    public List<Trigger> triggers(List<JobDetail> jobDetails) {
//        List<Trigger> triggers = new ArrayList<>();
//
//        for (JobDetail jobDetail : jobDetails) {
//            String jobName = jobDetail.getKey().getName().replace("_job", "");
//
//            String cron = scheduleProperties.getJobs()..getCron();
//            Trigger trigger = JobSchedulerHelper.buildCronTrigger(jobDetail, jobName, cron);
//            triggers.add(trigger);
//        }
//
//        return triggers;
//    }
//
//}

@Configuration
@EnableConfigurationProperties(ScheduleProperties.class)
@RequiredArgsConstructor
public class QuartzConfig {

    private final ScheduleProperties scheduleProperties;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(List<JobBase> jobs) {
        List<JobDetail> jobDetails = new ArrayList<>();
        List<Trigger> triggers = new ArrayList<>();

        for (JobBase job : jobs) {
            Optional.ofNullable(scheduleProperties.getJobs().get(job.getBeanName())).ifPresent(jobConfig -> {
                Class<? extends Job> jobClass = job.getClass();

                JobDetail jobDetail = JobSchedulerHelper.buildJobDetail(jobClass, job.getBeanName());
                Trigger trigger = JobSchedulerHelper.buildCronTrigger(jobDetail, job.getBeanName(), jobConfig.getCron());

                jobDetails.add(jobDetail);
                triggers.add(trigger);
            });
        }

        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobDetails(jobDetails.toArray(new JobDetail[0]));
        factory.setTriggers(triggers.toArray(new Trigger[0]));
        return factory;
    }
}