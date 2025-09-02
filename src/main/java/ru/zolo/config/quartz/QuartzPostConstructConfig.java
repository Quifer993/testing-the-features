package ru.zolo.config.quartz;

import lombok.RequiredArgsConstructor;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerListener;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import ru.zolo.properties.ScheduleProperties;
import ru.zolo.schedule.JobSchedulerHelper;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class QuartzPostConstructConfig {
    private final SchedulerFactoryBean schedulerFactory;
    private final TriggerListener triggerListener;
    private final ScheduleProperties scheduleProperties;
    private final JobSchedulerHelper jobSchedulerHelper;

    @PostConstruct
    public void addListener() throws SchedulerException {
        schedulerFactory.getScheduler().getListenerManager().addTriggerListener(triggerListener);
    }

    @PostConstruct
    private void clearUnusedJobs() {
        Set<String> activeJobNames = new HashSet<>();
        scheduleProperties.getJobs().forEach((name, jobConfig) -> {
            activeJobNames.add(name);
        });

        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            Set<JobKey> existingJobKeys = scheduler.getJobKeys(GroupMatcher.groupEquals(jobSchedulerHelper.getAppName()));

            for (JobKey jobKey : existingJobKeys) {
                if (!activeJobNames.contains(jobKey.getName())) {
                    scheduler.deleteJob(jobKey);
                }
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }

    }
}
