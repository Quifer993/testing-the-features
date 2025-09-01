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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class QuartzConfigFactoryBean {
    private final ScheduleProperties scheduleProperties;

    @Bean
    public SchedulerFactoryBeanCustomizer schedulerFactoryBeanCustomizer(List<Job> jobs) {
        return factory -> {
            List<JobDetail> jobDetails = new ArrayList<>();
            List<Trigger> triggers = new ArrayList<>();

            for (Job job2 : jobs) {
                if (JobBase.class.isAssignableFrom(job2.getClass())) {
                    JobBase job = (JobBase) job2;
                    Optional.ofNullable(scheduleProperties.getJobs().get(job.getBeanName())).ifPresent(jobConfig -> {
                        String cron = jobConfig.getCron();
                        if (cron != null && CronExpression.isValidExpression(cron)) {
                            //todo
                            //  Дано:
                            //      Cron: "0/5 * * * * ?" (каждые 5 сек)
                            //      Выполнение: 1-7 сек
                            //      Все потоки заняты в момент 12:00:00
                            //  Идеал:
                            //      Потоков нет → запуск откладывается
                            //      Потоки есть \ задача завершена -> запуск в 12 00 00
                            //      Потоки есть задача не завершена - > запуск в 12 00 05
                            CronScheduleBuilder cronExpression = CronScheduleBuilder.cronSchedule(cron)
//                                    .withMisfireHandlingInstructionFireAndProceed();
//                                    .withMisfireHandlingInstructionIgnoreMisfires();
                                    .withMisfireHandlingInstructionDoNothing();


                            Class<? extends Job> jobClass = job.getClass();

                            JobDetail jobDetail = JobSchedulerHelper.buildJobDetail(jobClass, job.getBeanName());
                            Trigger trigger = JobSchedulerHelper.buildCronTrigger(jobDetail, job.getBeanName(), cronExpression);

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