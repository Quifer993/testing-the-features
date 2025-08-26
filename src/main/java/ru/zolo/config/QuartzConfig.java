package ru.zolo.config;

import lombok.RequiredArgsConstructor;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import ru.zolo.properties.ScheduleProperties;
import ru.zolo.schedule.JobSchedulerHelper;
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
                String cron = jobConfig.getCron();
                if(cron != null && CronExpression.isValidExpression(cron)) {
                    //Пример:
                    //  Cron: "0/5 * * * * ?" (каждые 5 сек)
                    //  Выполнение: 1 сек
                    //  Все потоки заняты в момент 12:00:00
                    //-----идеал:
                    //
                    //  Потоков нет → запуск откладывается
                    //  Потоки есть \ задача завершена -> запуск в 12 00 00
                    //  Потоки есть задача не завершена - > запуск в 12 00 05
                    CronScheduleBuilder cronExpression = CronScheduleBuilder.cronSchedule(cron);
//                            .withMisfireHandlingInstructionFireAndProceed();


                    Class<? extends Job> jobClass = job.getClass();

                    JobDetail jobDetail = JobSchedulerHelper.buildJobDetail(jobClass, job.getBeanName());
                    Trigger trigger = JobSchedulerHelper.buildCronTrigger(jobDetail, job.getBeanName(), cronExpression);

                    jobDetails.add(jobDetail);
                    triggers.add(trigger);
                }
            });
        }

        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobDetails(jobDetails.toArray(new JobDetail[0]));
        factory.setTriggers(triggers.toArray(new Trigger[0]));
        return factory;
    }
}