package com.mycode.telegramagent.utils;

import lombok.SneakyThrows;

import java.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ExpiredDateGenerator {


    @SneakyThrows
    public static LocalDateTime getExpiredDate(String beginTime, String endTime, int expiredTime, String[] workingDays) {
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
        if (now.after(end)) {
            return nowAfterEnd(requestMinutes, diffMinutes, start, workingDays);
        } else {
            if (diffMinutes >= requestMinutes && isInWorkHour >= requestMinutes) {
                long day = 0;
                day = getDayInWorkDays(day, workingDays);
                return localDateTime.plusDays(day).plusMinutes(requestMinutes);
            } else if (diffMinutes >= requestMinutes && isInWorkHour < requestMinutes) {
                return isNotWorkingHours(requestMinutes, diffMinutes, isInWorkHour, localDateTime,workingDays);
            } else if (diffMinutes < requestMinutes) {
                return reqMinBigThanDiffMin(requestMinutes, diffMinutes, isInWorkHour, start,workingDays);
            }
        }


        return null;
    }


    public static LocalDateTime nowAfterEnd(long requestMinutes, long diffMinutes, Date start, String[] workingDays) {
        long day = requestMinutes / diffMinutes;
        long theRestMinute = requestMinutes - diffMinutes * day;
        day = day + 1;
        day = getDayInWorkDaysAfterNow(day, workingDays);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(start);
        return LocalDateTime.now().withHour(calendar.get(Calendar.HOUR_OF_DAY))
                .withMinute(calendar.get(Calendar.MINUTE)).plusDays(day).plusMinutes(theRestMinute);
    }


    public static LocalDateTime isNotWorkingHours(long requestMinutes, long diffMinutes, long isInWorkHour,
                                                  LocalDateTime localDateTime, String[] workingDays) {
        requestMinutes = requestMinutes - isInWorkHour;
        requestMinutes = diffMinutes - requestMinutes;
        int minute = Integer.valueOf(String.valueOf(isInWorkHour));
        int withMinute = localDateTime.getMinute() + minute;
        long day = 1;
        day = getDayInWorkDays(day, workingDays);
        return LocalDateTime.now().withHour(localDateTime.plusMinutes(minute).getHour())
                .withMinute(withMinute % 10).plusDays(day).minusMinutes(requestMinutes);
    }


    public static LocalDateTime reqMinBigThanDiffMin(long requestMinutes, long diffMinutes, long isInWorkHour, Date start, String[] workingDays) {
        requestMinutes = requestMinutes - isInWorkHour;
        long day = requestMinutes / diffMinutes;
        long theRestMinute = requestMinutes - diffMinutes * day;
        day = day + 1;
        day = getDayInWorkDays(day, workingDays);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(start);
        return LocalDateTime.now().withHour(calendar.get(Calendar.HOUR_OF_DAY))
                .withMinute(calendar.get(Calendar.MINUTE)).plusDays(day).plusMinutes(theRestMinute);
    }


    public static long getDayInWorkDays(Long day, String[] weekdays) {
        Date date = new Date();
        int d = 0;
        for (int i = 0; i <= day; i++) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, i * 24);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (Arrays.stream(weekdays).anyMatch(a -> a.equals(String.valueOf(weekDay)))) {
            } else {
                d++;
            }
        }
        d = checkOtherWeekDays(d, day, date, weekdays);

        return d + day;
    }

    public static long getDayInWorkDaysAfterNow(Long day, String[] weekdays) {
        Date date = new Date();
        int d = 0;
        for (int i = 1; i <= day; i++) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, i * 24);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (!Arrays.stream(weekdays).anyMatch(a -> a.equals(String.valueOf(weekDay)))) {
                d++;
            }
        }
        d = checkOtherWeekDays(d, day, date, weekdays);
        return d + day;
    }

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
