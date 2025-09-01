package ru.zolo.utils.annotation.quartz;

import org.springframework.stereotype.Indexed;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface ScheduledProperties {

    /**
     * The value indicate a suggestion for scheduled component name.
     * @return the suggested component name
     */
    String name();
}
