package com.mycode.telegramagent.utils;

import lombok.SneakyThrows;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote Every returned second of LocalDateTime must be 59
 */


public class ExpiredDateGenerator {


    /**
     * Expired date generator method
     *
     * @param beginTime   working hour beginning time,see application.properties
     * @param endTime     working hour ending time,see application.properties
     * @param expiredTime life time of request
     * @param workingDays working day,see application.properties
     * @return LocalDateTime
     */

    @SneakyThrows
    public static LocalDateTime getExpiredDate(String beginTime, String endTime, int expiredTime, String[] workingDays) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date start = format.parse(beginTime);
        Date end = format.parse(endTime);
        long difference = end.getTime() - start.getTime();
        long diffMinutes = difference / (60 * 1000);
        long requestMinutes = expiredTime * 60L;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime localDateTime = LocalDateTime.now();
        String formatDateTime = localDateTime.format(dtf);
        Date now = format.parse(formatDateTime);
        long diffEndTimeNow = end.getTime() - now.getTime();
        long isInWorkHour = diffEndTimeNow / (60 * 1000);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(Date.from(Instant.now()));
        if (Arrays.stream(workingDays).noneMatch(a -> a.equals(String.valueOf(calendar.get(Calendar.DAY_OF_WEEK) - 1)))) {
            return notWorkingDay(requestMinutes, diffMinutes, start, workingDays);
        } else if (now.after(end)) {
            return nowAfterEnd(requestMinutes, diffMinutes, start, workingDays);
        } else {
            if (diffMinutes >= requestMinutes && isInWorkHour >= requestMinutes) {
                long day = 0;
                day = getDayInWorkDays(day, workingDays);
                return localDateTime.withSecond(59).withNano(0).plusDays(day).plusMinutes(requestMinutes);
            } else if (diffMinutes >= requestMinutes && isInWorkHour < requestMinutes) {
                return isNotWorkingHours(requestMinutes, diffMinutes, isInWorkHour, localDateTime, workingDays);
            } else if (diffMinutes < requestMinutes) {
                return reqMinBigThanDiffMin(requestMinutes, diffMinutes, isInWorkHour, start, workingDays);
            }
        }


        return null;
    }

    /**
     * This method for if LocalDateTime.now() day not matched with working day
     *
     * @param requestMinutes life time of request(convert hour to minute)
     * @param diffMinutes    difference  minutes between start and end working hour
     * @param start          working hour beginning time,see application.properties
     * @param workingDays    working days,see application.properties
     * @return LocalDateTime
     */

    private static LocalDateTime notWorkingDay(long requestMinutes, long diffMinutes,
                                               Date start, String[] workingDays) {
        long day = requestMinutes / diffMinutes;
        long theRestMinute = requestMinutes - diffMinutes * day;
        day = 0;
        day = getDayInWorkDays(day, workingDays);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(start);
        return LocalDateTime.now().withSecond(59).withNano(0).withHour(calendar.get(Calendar.HOUR_OF_DAY))
                .withMinute(calendar.get(Calendar.MINUTE)).plusDays(day).plusMinutes(theRestMinute);
    }

    /**
     * This method for if LocalDateTime.now() day is in working day but not in working hour
     *
     * @param requestMinutes life time of request(convert hour to minute)
     * @param diffMinutes    difference  minutes between start and end working hour
     * @param start          working hour beginning time,see application.properties
     * @param workingDays    working days,see application.properties
     * @return LocalDateTime
     */

    public static LocalDateTime nowAfterEnd(long requestMinutes, long diffMinutes, Date start, String[] workingDays) {
        long day = requestMinutes / diffMinutes;
        long theRestMinute = requestMinutes - diffMinutes * day;
        day = day + 1;
        day = getDayInWorkDaysAfterNow(day, workingDays);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(start);
        return LocalDateTime.now().withSecond(59).withNano(0).withHour(calendar.get(Calendar.HOUR_OF_DAY))
                .withMinute(calendar.get(Calendar.MINUTE)).plusDays(day).plusMinutes(theRestMinute);
    }


    /**
     * This method for if LocalDateTime.now() day is in working day and in working hours.But not expired today
     *
     * @param requestMinutes life time of request(convert hour to minute)
     * @param diffMinutes    difference  minutes between start and end working hour
     * @param isInWorkHour   difference minutes between end and now
     * @param workingDays    working day see,application.properties
     * @param localDateTime  LocalDateTime.now()
     * @return LocalDateTime
     */

    public static LocalDateTime isNotWorkingHours(long requestMinutes, long diffMinutes, long isInWorkHour,
                                                  LocalDateTime localDateTime, String[] workingDays) {
        requestMinutes = requestMinutes - isInWorkHour;
        requestMinutes = diffMinutes - requestMinutes;
        int minute = Integer.parseInt(String.valueOf(isInWorkHour));
        int withMinute = localDateTime.getMinute() + minute;
        long day = 1;
        day = getDayInWorkDays(day, workingDays);
        return LocalDateTime.now().withSecond(59).withNano(0).withHour(localDateTime.plusMinutes(minute).getHour())
                .withMinute(withMinute % 10).plusDays(day).minusMinutes(requestMinutes);
    }


    /**
     * This method for if expired day must be minimum 2 day after
     *
     * @param requestMinutes life time of request(convert hour to minute)
     * @param diffMinutes    difference  minutes between start and end working hour
     * @param isInWorkHour   difference minutes between end and now
     * @param workingDays    working day see application.properties
     * @param start          working hour beginning time,see application.properties
     * @return LocalDateTime
     */

    public static LocalDateTime reqMinBigThanDiffMin(long requestMinutes, long diffMinutes, long isInWorkHour, Date start, String[] workingDays) {
        requestMinutes = requestMinutes - isInWorkHour;
        long day = requestMinutes / diffMinutes;
        long theRestMinute = requestMinutes - diffMinutes * day;
        day = day + 1;
        day = getDayInWorkDays(day, workingDays);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(start);
        return LocalDateTime.now().withSecond(59).withNano(0).withHour(calendar.get(Calendar.HOUR_OF_DAY))
                .withMinute(calendar.get(Calendar.MINUTE)).plusDays(day).plusMinutes(theRestMinute);
    }


    /**
     * This method for checks if LocalDateTime.now() day is in working day or not
     *
     * @param day day which not checked for working days
     * @param weekdays    working day,see application.properties
     * @return LocalDateTime
     */

    public static long getDayInWorkDays(Long day, String[] weekdays) {
        Date date = new Date();
        int d = 0;
        for (int i = 0; i <= day; i++) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, i * 24);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (Arrays.stream(weekdays).noneMatch(a -> a.equals(String.valueOf(weekDay)))) {
                d++;
            }
        }
        d = checkOtherWeekDays(d, day, date, weekdays);

        return d + day;
    }

    /**
     * This method for checks if LocalDateTime.now() day is in working day or not(After now)
     *
     * @param day day which not checked for working days
     * @param weekdays    working day,see application.properties
     * @return LocalDateTime
     */

    public static long getDayInWorkDaysAfterNow(Long day, String[] weekdays) {
        Date date = new Date();
        int d = 0;
        for (int i = 1; i <= day; i++) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, i * 24);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (Arrays.stream(weekdays).noneMatch(a -> a.equals(String.valueOf(weekDay)))) {
                d++;
            }
        }
        d = checkOtherWeekDays(d, day, date, weekdays);
        return d + day;
    }

    /**
     * This method for checks if LocalDateTime.now() day is in working day or not(Last checker)
     *
     * @param day day which not checked for working days
     * @param weekdays    working day,see application.properties
     * @return LocalDateTime
     */

    public static int checkOtherWeekDays(int d, Long day, Date date, String[] weekdays) {
        for (long i = d + day; i <= 7; i++) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, Math.toIntExact(i * 24));
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (Arrays.stream(weekdays).anyMatch(a -> a.equals(String.valueOf(weekDay)))) {
                break;
            } else {
                d++;
            }
        }
        return d;
    }
}
