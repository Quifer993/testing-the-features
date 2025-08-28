package ru.zolo.config.quartz.listiner;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Profile("quartz")
public class QuartzTriggerListener implements TriggerListener {
    @Override
    public String getName() {
        return "Quifer-очек слушает";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext jobExecutionContext) {
//        System.out.println(String.format("Trigger %s was fired", trigger.getKey()));
        System.out.println();
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext jobExecutionContext) {
//        System.out.printf("Trigger %s vetoed: --- ", trigger.getKey());
//        System.out.println();
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
//        System.out.println(String.format("Trigger %s misfired, time : %s", trigger.getKey(), LocalDateTime.now()));
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext jobExecutionContext, Trigger.CompletedExecutionInstruction completedExecutionInstruction) {
        System.out.printf("Trigger %s completed: +++", trigger.getKey());
        System.out.println();
    }
}
