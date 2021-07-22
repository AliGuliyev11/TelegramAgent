package com.mycode.telegramagent.utils;

import lombok.SneakyThrows;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Configuration
public class ExpiredDateGenerator {


    @SneakyThrows
    public static LocalDateTime getExpiredDate(String beginTime, String endTime, int expiredTime) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date start = format.parse(beginTime);
        Date end = format.parse(endTime);
        long difference = end.getTime() - start.getTime();
        long diffMinutes = difference / (60 * 1000);
        long requestMinutes = expiredTime * 60;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime localDateTime = LocalDateTime.now();
        String formatDateTime = localDateTime.format(dtf);
        Date now = format.parse(formatDateTime);
        long diffEndTimeNow = end.getTime() - now.getTime();
        long isInWorkHour = diffEndTimeNow / (60 * 1000);
        if (now.after(end)){
            long day = requestMinutes / diffMinutes;
            long theRestMinute = requestMinutes - diffMinutes * day;
            day = day + 1;
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(start);
            return LocalDateTime.now().withHour(calendar.get(Calendar.HOUR_OF_DAY))
                    .withMinute(calendar.get(Calendar.MINUTE)).plusDays(day).plusMinutes(theRestMinute);
        }else{
            if (diffMinutes >= requestMinutes && isInWorkHour >= requestMinutes) {
                return localDateTime.plusMinutes(requestMinutes);
            } else if (diffMinutes >= requestMinutes && isInWorkHour < requestMinutes) {
                requestMinutes = requestMinutes - isInWorkHour;
                requestMinutes = diffMinutes - requestMinutes;
                int minute = Integer.valueOf(String.valueOf(isInWorkHour));
                int withMinute = localDateTime.getMinute() + minute;
                return LocalDateTime.now().withHour(localDateTime.plusMinutes(minute).getHour())
                        .withMinute(withMinute % 10).plusDays(1).minusMinutes(requestMinutes);
            } else if (diffMinutes < requestMinutes) {
                requestMinutes = requestMinutes - isInWorkHour;
                long day = requestMinutes / diffMinutes;
                long theRestMinute = requestMinutes - diffMinutes * day;
                day = day + 1;
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTime(start);
                return LocalDateTime.now().withHour(calendar.get(Calendar.HOUR_OF_DAY))
                        .withMinute(calendar.get(Calendar.MINUTE)).plusDays(day).plusMinutes(theRestMinute);
            }
        }


        return null;
    }
}
