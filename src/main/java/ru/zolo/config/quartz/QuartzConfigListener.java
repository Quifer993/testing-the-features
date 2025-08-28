package ru.zolo.config.quartz;

import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.quartz.TriggerListener;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import ru.zolo.config.quartz.listiner.QuartzTriggerListener;
import ru.zolo.properties.ScheduleProperties;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class QuartzConfigListener {
    private final SchedulerFactoryBean schedulerFactory;
    private final TriggerListener triggerListener;

    @PostConstruct
    public void addListener() throws SchedulerException {
        schedulerFactory.getScheduler().getListenerManager().addTriggerListener(triggerListener);
    }
}
