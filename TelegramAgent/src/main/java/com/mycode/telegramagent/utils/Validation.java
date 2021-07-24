package com.mycode.telegramagent.utils;

import com.mycode.telegramagent.dto.OfferDto;
import com.mycode.telegramagent.exceptions.*;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

public class Validation {

    private static final String DATE_REGEX = "\\d{4}[-]\\d{2}[-]\\d{2}";

    @SneakyThrows
    public static void validation(OfferDto offerDto) {


        if (!Pattern.matches(DATE_REGEX, offerDto.getStartDate()) || !Pattern.matches(DATE_REGEX, offerDto.getEndDate())) {
            throw new DateFormat();
        }
        if (offerDto.getDescription() == null || offerDto.getStartDate() == null || offerDto.getEndDate() == null
                || offerDto.getPrice() == null) {
            throw new OfferValidation();
        }
        if (offerDto.getPrice() <= 0) {
            throw new OfferPriceZero();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = simpleDateFormat.parse(offerDto.getStartDate());
        Date endDate = simpleDateFormat.parse(offerDto.getEndDate());
        Date date = new Date();
        String d = simpleDateFormat.format(date);
        if (startDate.before(date) && !simpleDateFormat.format(startDate).equals(d)) {
            throw new OfferDateBeforeNow();
        }
        if (startDate.after(endDate)) {
            throw new EndDateBeforeStart();
        }
    }

    @SneakyThrows
    public static void checkOfferMadaInWorkingHours(String beginTime, String endTime, String[] workingDays) {
        Date now = new Date();
        Calendar calendarNow = GregorianCalendar.getInstance();
        calendarNow.setTime(now);
        String weekday = String.valueOf(calendarNow.get(Calendar.DAY_OF_WEEK) - 1);
        if (!Arrays.stream(workingDays).anyMatch(a -> a.equals(weekday))) {
            throw new OfferNotWorkingHour();
        }

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date start = format.parse(beginTime);
        Date end = format.parse(endTime);

        Calendar calendarStart = GregorianCalendar.getInstance();
        calendarStart.setTime(start);
        Calendar calendarEnd = GregorianCalendar.getInstance();
        calendarEnd.setTime(end);

        int hour = calendarNow.get(Calendar.HOUR_OF_DAY);
        int minute = calendarNow.get(Calendar.MINUTE);
        int diff=(24-hour)*60-minute;

        if (diff < (24-calendarEnd.get(Calendar.HOUR_OF_DAY))*60-calendarEnd.get(Calendar.MINUTE) ||
                diff > (24-calendarStart.get(Calendar.HOUR_OF_DAY))*60-calendarStart.get(Calendar.MINUTE)) {
            throw new OfferNotWorkingHour();
        }
    }
}
