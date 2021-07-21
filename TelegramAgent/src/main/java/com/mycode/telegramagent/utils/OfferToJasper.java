package com.mycode.telegramagent.utils;

import com.mycode.telegramagent.dto.JasperDto;
import com.mycode.telegramagent.dto.OfferDto;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class OfferToJasper {
    public static JasperDto offerToJasper(String agency, OfferDto offerDto) {
        String pattern = "dd.MM.yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String startDate = simpleDateFormat.format(offerDto.getStartDate());
        String endDate = simpleDateFormat.format(offerDto.getEndDate());
        String dateRange = startDate + "-" + endDate;
        JasperDto jasperDto = JasperDto.builder().AGENCY(agency).description(offerDto.getDescription())
                .note(offerDto.getNote()).price(offerDto.getPrice()).dateRange(dateRange).build();
        return jasperDto;
    }
}
