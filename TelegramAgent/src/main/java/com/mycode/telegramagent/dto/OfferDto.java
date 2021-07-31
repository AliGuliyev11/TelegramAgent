package com.mycode.telegramagent.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class OfferDto {
    String description;
    String startDate;
    String endDate;
    String note;
    Double price;
}
