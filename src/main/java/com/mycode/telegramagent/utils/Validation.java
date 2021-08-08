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

/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote Class for validation
 */

public class Validation {

    private static final String DATE_REGEX = "\\d{4}[-]\\d{2}[-]\\d{2}";

    /**
     * This method for offer validation
     *
     * @param offerDto    offer which make agent
     * @param orderBudget user requested maximum budget
     * @throws DateFormat         if date format not implemented correctly
     * @throws OfferValidation    if offer filed null except note
     * @throws OfferPriceZero     if agent offers 0 or negative price
     * @throws OfferBudgetHigher  if price of offer too high than user requested budget
     * @throws OfferDateBeforeNow if now not present or future tense
     * @throws EndDateBeforeStart if agent offered end date before start date
     */

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
            throw new OfferBudgetHigher(orderBudget);
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

    /**
     * This method check offer made in working hours or not
     *
     * @param beginTime   working hour beginning time,see application.properties
     * @param endTime     working hour ending time,see application.properties
     * @param workingDays working day,see application.properties
     * @throws OfferNotWorkingHour if offer not in working day or hour
     */

    @SneakyThrows
    public static void checkOfferMadaInWorkingHours(String beginTime, String endTime, String[] workingDays) {
        Date now = new Date();
        Calendar calendarNow = GregorianCalendar.getInstance();
        calendarNow.setTime(now);
        String weekday = String.valueOf(calendarNow.get(Calendar.DAY_OF_WEEK) - 1);
        if (Arrays.stream(workingDays).noneMatch(a -> a.equals(weekday))) {
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

    /**
     * This method for agent sign up validation
     *
     * @param agentDto agent sign up DTO
     * @throws AgentValidation    if agent required fields is null
     * @throws PasswordNotMatched if agent password filed not matched with re-password field
     */

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
    private static final String PHONE_REGEX = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}|(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}|(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";
    private static final String PASSWORD_REGEX = ".{8,}";
    private static final String VOEN_REGEX = "[0-9]{10}";


    /**
     * This method for validation regex
     *
     * @param agentDto agent sign up DTO
     * @throws EmailValidation    if email regex not matched
     * @throws PhoneValidation    if phone regex not matched
     * @throws PasswordValidation if password regex not matched
     * @throws VoenValidation     if voen not null and regex not matched
     */

    public static void regexValidation(AgentDto agentDto) {
        if (!matchedRegex(agentDto.getEmail(), EMAIL_REGEX)) {
            throw new EmailValidation();
        } else if (!matchedRegex(agentDto.getPhoneNumber(), PHONE_REGEX)) {
            throw new PhoneValidation();
        } else if (!matchedRegex(agentDto.getPassword(), PASSWORD_REGEX)) {
            throw new PasswordValidation();
        } else if (agentDto.getVoen() != null && !matchedRegex(agentDto.getVoen(), VOEN_REGEX)) {
            throw new VoenValidation();
        }
    }

    /**
     * This method matches regex with agent fields
     */

    public static boolean matchedRegex(String emailOrNumber, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(emailOrNumber);
        return matcher.matches();
    }

    /**
     * This method checks offer end date
     *
     * @param endDate     offer end date
     * @param orderDate   user requested start date
     * @param orderDateTo user requested end date
     * @throws CheckStartDate if end date not include this orderDate and orderDateTo range
     * @implNote User requested date some date range and if end date not include this range program throws exception
     */

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

    /**
     * This method checks offer start date
     *
     * @param startDate   offer start date
     * @param orderDate   user requested start date
     * @param orderDateTo user requested end date
     * @throws CheckStartDate if start date not include this orderDate and orderDateTo range
     * @implNote Offer start date after user requested start date and not include user requested start and end date range
     */

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
