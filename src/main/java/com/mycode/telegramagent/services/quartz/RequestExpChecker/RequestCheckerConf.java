package com.mycode.telegramagent.services.quartz.RequestExpChecker;

import lombok.SneakyThrows;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.Calendar;

/**
 * @author Ali Guliyev
 * @version 1.0
 * Expired request checker component class
 */

@Component
public class RequestCheckerConf {

    @Value("${work.begin.time}")
    String beginTime;
    @Value("${work.end.time}")
    String endTime;
    @Value("${working.days}")
    String[] workingDays;


    @Bean
    public JobDetail checker() {
        return JobBuilder.newJob(RequestChecker.class)
                .withIdentity("requestChecker", "requestCheckerJob")
                .storeDurably(true)
                .build();
    }

    @SneakyThrows
    @Bean
    public Trigger triggerChecker(@Qualifier("checker") JobDetail jobDetail) {

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

        Date now = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(now);
        String weekday = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK) - 1);
        if (Arrays.stream(workingDays).noneMatch(a -> a.equals(weekday))) {
            return null;
        } else {
            return TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity("requestChecker", "requestCheckerTrigger")
                    .endAt(Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant()))
                    .startAt(Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant()))
                    .withSchedule(CronScheduleBuilder.cronSchedule("59 * * ? * * *").inTimeZone(TimeZone.getTimeZone("GMT"))
                    ).build();
        }
    }
}
