package ru.zolo.schedule.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.Serializable;

public abstract class JobBase implements Job, Serializable {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

    }
}
