package ru.zolo.config.quartz;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobListener;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.quartz.impl.matchers.KeyMatcher;
import org.quartz.utils.ConnectionProvider;
import org.quartz.utils.DBConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import ru.zolo.properties.ScheduleProperties;
import ru.zolo.schedule.JobSchedulerHelper;
import ru.zolo.schedule.jobs.JobBase;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.quartz.JobKey.jobKey;

@Configuration
@EnableConfigurationProperties(ScheduleProperties.class)
@RequiredArgsConstructor
public class QuartzConfig {

    private final ScheduleProperties scheduleProperties;

    private final SchedulerFactoryBean schedulerFactory;
//    private final JobListener jobListener;
    private final TriggerListener triggerListener;

//    @PostConstruct
//    public void addListener() throws SchedulerException {
//        schedulerFactory.getScheduler().getListenerManager().addTriggerListener(triggerListener);
//    }

    @Bean
    @ConfigurationProperties("datasource-quartz")
    DataSourceProperties quartzDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("quartzDataSource")
    @ConfigurationProperties("datasource-quartz.configuration")
    @QuartzDataSource
    public DataSource quartzDataSource(){
        return quartzDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @PostConstruct
    public void registerQuartzConnectionProvider(@Qualifier("quartzDataSource") DataSource quartzDataSource) {
        DBConnectionManager.getInstance().addConnectionProvider("quartzDataSource", new ConnectionProvider() {
            @Override
            public Connection getConnection() throws SQLException {
                return quartzDataSource.getConnection();
            }
            @Override public void shutdown() {}
            @Override public void initialize() {}
        });
    }

    @Bean
    public SchedulerFactoryBeanCustomizer schedulerFactoryBeanCustomizer(List<Job> jobs, DataSource dataSource) {
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
//                            Trigger trigger = JobSchedulerHelper.cronTrigger(jobDetail, job.getBeanName(), cron);

                            jobDetails.add(jobDetail);
                            triggers.add(trigger);
                        }
                    });
                }
            }

            factory.setJobDetails(jobDetails.toArray(new JobDetail[0]));
            factory.setTriggers(triggers.toArray(new Trigger[0]));
            setProperty(factory, "org.quartz.threadPool.threadCount", Integer.valueOf(triggers.size()).toString());
        };
    }

    private static void setProperty(SchedulerFactoryBean factory, String name, String value){
        try {
            Field fieldQuartzProperties = SchedulerFactoryBean.class.getDeclaredField("quartzProperties");
            fieldQuartzProperties.setAccessible(true);
            Properties prop = (Properties)fieldQuartzProperties.get(factory);
            prop.put(name, value);
            fieldQuartzProperties.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}