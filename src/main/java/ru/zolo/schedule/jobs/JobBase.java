package ru.zolo.schedule.jobs;

import lombok.Getter;
import org.quartz.Job;
import org.springframework.beans.factory.BeanNameAware;
import ru.zolo.properties.ScheduleProperties;

import java.io.Serializable;

public abstract class JobBase implements Job, BeanNameAware, Serializable {
    @Getter
    transient protected String beanName;

    transient private ScheduleProperties scheduleProperties;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

}
