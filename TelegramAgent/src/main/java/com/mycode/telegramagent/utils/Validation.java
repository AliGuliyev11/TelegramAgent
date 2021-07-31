package com.mycode.telegramagent.utils;

import com.mycode.telegramagent.dto.AgentDto;
import com.mycode.telegramagent.dto.OfferDto;
import com.mycode.telegramagent.exceptions.*;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    private static final String DATE_REGEX = "\\d{4}[-]\\d{2}[-]\\d{2}";

    @SneakyThrows
    public static void validation(OfferDto offerDto, int orderBudget) {


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
        if (offerDto.getPrice() > orderBudget) {
            throw new OfferBudgetHigher();
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
        int diff = (24 - hour) * 60 - minute;

        if (diff < (24 - calendarEnd.get(Calendar.HOUR_OF_DAY)) * 60 - calendarEnd.get(Calendar.MINUTE) ||
                diff > (24 - calendarStart.get(Calendar.HOUR_OF_DAY)) * 60 - calendarStart.get(Calendar.MINUTE)) {
            throw new OfferNotWorkingHour();
        }
    }

    @SneakyThrows
    public static void signUpValidation(AgentDto agentDto) {
        if (agentDto.getEmail() == null || agentDto.getPassword() == null || agentDto.getAgencyName() == null
                || agentDto.getCompanyName() == null || agentDto.getPhoneNumber() == null || agentDto.getRepass() == null) {
            throw new AgentValidation();
        }

        if (!agentDto.getPassword().equals(agentDto.getRepass())) {
            throw new PasswordNotMatched();
        }
    }


    private static final String EMAIL_REGEX = "([a-zA-Z0-9_.+-])+\\@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})";
    private static final String PHONE_REGEX = "[+]{1}[9]{2}[4]{1}(([5]([0]|[1]|[5]))|([7]([0]|[7]))|([9]([9])))[1-9][0-9]{6}";
    private static final String PASSWORD_REGEX = ".{8,}";
    private static final String VOEN_REGEX = "[0-9]{10}";


    public static void regexValidation(AgentDto agentDto) {
        if (!isMatchedRegex(agentDto.getEmail(), EMAIL_REGEX)) {
            throw new EmailValidation();
        } else if (!isMatchedRegex(agentDto.getPhoneNumber(), PHONE_REGEX)) {
            throw new PhoneValidation();
        } else if (!isMatchedRegex(agentDto.getPassword(), PASSWORD_REGEX)) {
            throw new PasswordValidation();
        } else if (agentDto.getVoen()!=null && !isMatchedRegex(agentDto.getVoen(), VOEN_REGEX)) {
            throw new VoenValidation();
        }
    }


    public static boolean isMatchedRegex(String emailOrNumber, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(emailOrNumber);
        return matcher.matches();
    }

    @SneakyThrows
    public static void checkEndDate(String endDate, String orderDate, long orderDateTo) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date end = simpleDateFormat.parse(endDate);
        Date orderEnd = simpleDateFormat.parse(orderDate);
        long days = ChronoUnit.DAYS.between(LocalDateTime.ofInstant(orderEnd.toInstant(), ZoneId.systemDefault())
                , LocalDateTime.ofInstant(end.toInstant(), ZoneId.systemDefault()));
        System.out.println(days);
        if (days > orderDateTo) {
            throw new CheckStartDate("Your end date must be equal start date or after " + orderDateTo + " days from this "
                    + simpleDateFormat.format(orderEnd));
        }
    }

    @SneakyThrows
    public static void checkStartDate(String startDate, String orderDate, Long orderDateTo) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start = simpleDateFormat.parse(startDate);
        Date end = simpleDateFormat.parse(orderDate);
        if (start.before(end) && !orderDate.equals(startDate)) {
            throw new CheckStartDate("Your start date must be equal or after " + simpleDateFormat.format(end));
        }
        long days = ChronoUnit.DAYS.between(LocalDateTime.ofInstant(end.toInstant(), ZoneId.systemDefault())
                , LocalDateTime.ofInstant(start.toInstant(), ZoneId.systemDefault()));
        System.out.println(days);
        if (days > orderDateTo) {
            throw new CheckStartDate("Your start date must be equal " + simpleDateFormat.format(end) + " or after max " + orderDateTo + " days");
        }
    }

}
