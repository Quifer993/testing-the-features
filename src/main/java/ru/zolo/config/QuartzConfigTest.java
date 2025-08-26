//package ru.zolo.config;
//
//import org.quartz.*;
//import org.quartz.spi.JobFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.scheduling.quartz.JobDetailFactoryBean;
//import org.springframework.scheduling.quartz.SchedulerFactoryBean;
//import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
//import org.springframework.scheduling.quartz.SpringBeanJobFactory;
//import ru.zolo.schedule.jobs.SampleJob;
//
//@Configuration
//public class QuartzConfigTest {
////
////    @SneakyThrows
////    @Bean(name = "quartzSchedulerr")
////    public Scheduler getScheduler(Scheduler scheduler) {
////        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
////
////        scheduler.start();
////        return schedulerFactory.getScheduler();
////    }
//
////    @Bean
////    public JobDetail jobDetail() {
////        return JobBuilder.newJob().ofType(SampleJob.class)
////                .storeDurably()
////                .withIdentity("Qrtz_Job_Detail")
////                .withDescription("Invoke Sample Job service...")
////                .build();
////    }
//
//
//    //    @Bean
////    public Trigger trigger(JobDetail job) {
////        return TriggerBuilder.newTrigger().forJob(job)
////                .withIdentity("Qrtz_Trigger")
////                .withDescription("Sample trigger")
////                .withSchedule(simpleSchedule().repeatForever().withIntervalInHours(1))
////                .build();
////    }
//
//    @Bean
//    public JobDetailFactoryBean jobDetailFactoryBean() {
//        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
//        jobDetailFactory.setJobClass(SampleJob.class);
//        jobDetailFactory.setDescription("Invoke Sample Job service...");
//        jobDetailFactory.setDurability(true);
//        return jobDetailFactory;
//    }
//
//
//    @Bean
//    public SimpleTriggerFactoryBean trigger2(JobDetail job) {
//        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
//        trigger.setJobDetail(job);
//        trigger.setRepeatInterval(3600000);
//        trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
//        return trigger;
//    }
//
//    @Bean
//    public SchedulerFactoryBean scheduler(Trigger trigger, JobDetail job, JobFactory jobFactory/*, DataSource quartzDataSource*/) {
//        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
//        schedulerFactory.setConfigLocation(new ClassPathResource("quartz.properties"));
//
//        schedulerFactory.setJobFactory(SpringBeanJobFactory.class);
//        schedulerFactory.setJobDetails(job);
//        schedulerFactory.setTriggers(trigger);
////        schedulerFactory.setDataSource(quartzDataSource);
//        return schedulerFactory;
//    }
//
//    @Bean
//    public SpringBeanJobFactory springBeanJobFactory() {
//        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
//        jobFactory.setApplicationContext(applicationContext);
//        return jobFactory;
//    }
//}
