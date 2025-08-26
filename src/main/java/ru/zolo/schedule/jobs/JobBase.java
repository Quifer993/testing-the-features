package ru.zolo.schedule.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

abstract class JobBase implements Job {
    protected String cron;

    @Override
    public abstract void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException;
}
