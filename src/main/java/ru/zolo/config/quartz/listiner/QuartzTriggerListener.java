package ru.zolo.config.quartz.listiner;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("quartz")
public class QuartzTriggerListener implements TriggerListener {
    @Override
    public String getName() {
        return "";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext jobExecutionContext) {
        System.out.println(String.format("Trigger %s was fired", trigger.getKey()));
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext jobExecutionContext) {
        System.out.printf("Trigger %s vetoed", trigger.getKey());
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        System.out.println(String.format("Trigger %s misfired", trigger.getKey()));
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext jobExecutionContext, Trigger.CompletedExecutionInstruction completedExecutionInstruction) {
        System.out.printf("Trigger %s completed", trigger.getKey());
    }
}
