package ru.zolo.schedule.jobs;

import lombok.Getter;
import lombok.Setter;
import org.quartz.Job;
import org.springframework.beans.factory.BeanNameAware;
import ru.zolo.properties.ScheduleProperties;

import java.io.Serializable;

public abstract class JobBase implements Job, BeanNameAware, Serializable {
    @Getter
    @Setter
    transient protected String beanName;

}
