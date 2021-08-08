package com.mycode.telegramagent.services.quartz.DeleteExpired;

import lombok.SneakyThrows;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.TimeZone;

/**
 * @author Ali Guliyev
 * @version 1.0
 * Expired request component class
 */


@Component
public class RequestDeleteConf {

    @Bean(name = "delete_expired_job")
    public JobDetail expired() {
        return JobBuilder.newJob(RequestDelete.class)
                .withIdentity("deleteExpired", "deleteExpiredJob")
                .storeDurably(true)
                .build();
    }

    @SneakyThrows
    @Bean
    public Trigger triggerIssues(@Qualifier("delete_expired_job") JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("deleteExpiredChecker", "deleteExpiredTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 1 3 1/1 * ? *").inTimeZone(TimeZone.getTimeZone("GMT")))
                .build();

    }
}
