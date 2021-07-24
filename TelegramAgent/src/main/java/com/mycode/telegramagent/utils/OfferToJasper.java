package com.mycode.telegramagent.utils;

import com.mycode.telegramagent.dto.JasperDto;
import com.mycode.telegramagent.dto.OfferDto;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OfferToJasper {
    @SneakyThrows
    public static JasperDto offerToJasper(String agency, OfferDto offerDto) {
        SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
        Date start = toDate.parse(offerDto.getStartDate());
        Date end = toDate.parse(offerDto.getEndDate());
        SimpleDateFormat toString = new SimpleDateFormat("dd.MM.yyyy");
        String startDate = toString.format(start);
        String endDate = toString.format(end);
        String dateRange = startDate + "-" + endDate;
        JasperDto jasperDto = JasperDto.builder().AGENCY(agency).description(offerDto.getDescription())
                .note(offerDto.getNote()).price(offerDto.getPrice()).dateRange(dateRange).build();
        return jasperDto;
    }
}
