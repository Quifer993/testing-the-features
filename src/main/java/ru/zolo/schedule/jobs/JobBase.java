package ru.zolo.schedule.jobs;

import lombok.Getter;
import org.quartz.Job;
import org.springframework.beans.factory.BeanNameAware;
import ru.zolo.properties.ScheduleProperties;

public abstract class JobBase implements Job, BeanNameAware {
    @Getter
    protected String beanName;

    private ScheduleProperties scheduleProperties;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

}
