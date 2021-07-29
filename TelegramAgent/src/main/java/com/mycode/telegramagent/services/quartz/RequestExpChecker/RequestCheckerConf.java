package com.mycode.telegramagent.services.quartz.RequestExpChecker;

import com.mycode.telegramagent.exceptions.OfferNotWorkingHour;
import com.mycode.telegramagent.rabbitmq.offerOrder.publisher.RabbitOfferService;
import com.mycode.telegramagent.services.LifeCycle.OrderLifeCycle;
import lombok.SneakyThrows;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
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
    @Value("${working.days}")
    String[] workingDays;


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

        Date now = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(now);
        String weekday = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK) - 1);
        if (!Arrays.stream(workingDays).anyMatch(a -> a.equals(weekday))) {
            return null;
        } else {
            int second=LocalDateTime.now().getSecond();
            System.out.println(second);
            int s=60-second;
            System.out.println(s);
            return TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity("requestChecker", "requestCheckerTrigger")
                    .endAt(Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant()))
                    .startAt(Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant()))
                    .withSchedule(CronScheduleBuilder.cronSchedule("59 * * ? * * *")
                    ).build();
        }
    }
}
