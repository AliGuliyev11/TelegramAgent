package com.mycode.telegramagent.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Getter
@Setter
@Builder(toBuilder = true)
public class OfferDto {
    String description;
    Date startDate;
    Date endDate;
    String note;
    Double price;


}
