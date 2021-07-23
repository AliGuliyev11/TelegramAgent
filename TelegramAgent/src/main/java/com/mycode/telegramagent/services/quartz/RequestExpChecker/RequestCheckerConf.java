package com.mycode.telegramagent.services.quartz.RequestExpChecker;

import lombok.SneakyThrows;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@Component
public class RequestCheckerConf {

    @Value("${work.begin.time}")
    String beginTime;
    @Value("${work.end.time}")
    String endTime;

    @Bean
    public JobDetail issuesSync() {
        return JobBuilder.newJob(RequestChecker.class)
                .withIdentity("requestChecker", "requestCheckerJob")
                .storeDurably(true)
                .build();
    }

    @SneakyThrows
    @Bean
    public Trigger triggerIssues(JobDetail jobDetail) {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date start = format.parse(beginTime);
        Date end = format.parse(endTime);
        Calendar calendarStart = GregorianCalendar.getInstance();
        calendarStart.setTime(start);
        Calendar calendarEnd = GregorianCalendar.getInstance();
        calendarEnd.setTime(end);
        LocalDateTime startDate = LocalDateTime.now().withHour(calendarStart.get(Calendar.HOUR_OF_DAY))
                .withMinute(calendarStart.get(Calendar.MINUTE));
        LocalDateTime endDate = LocalDateTime.now().withHour(calendarEnd.get(Calendar.HOUR_OF_DAY))
                .withMinute(calendarEnd.get(Calendar.MINUTE));
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("requestChecker", "requestCheckerTrigger")
                .endAt(Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant()))
                .startAt(Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant()))
                .withSchedule(
                        simpleSchedule()
                                .withIntervalInMinutes(1)
                                .repeatForever()
                ).build();
    }
}
