package com.mycode.telegramagent.utils;

import com.mycode.telegramagent.dto.OfferDto;
import com.mycode.telegramagent.exceptions.*;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        if (startDate.before(date) && ! simpleDateFormat.format(startDate).equals(d)) {
            throw new OfferDateBeforeNow();
        }
        if (startDate.after(endDate)) {
            throw new EndDateBeforeStart();
        }

    }
}
