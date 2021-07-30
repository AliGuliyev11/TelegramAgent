package com.mycode.telegramagent.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Builder(toBuilder = true)
public class JasperDto {
    String description;
    String dateRange;
    String note;
    Double price;
}
