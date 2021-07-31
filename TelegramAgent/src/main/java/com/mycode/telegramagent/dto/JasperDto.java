package com.mycode.telegramagent.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote This DTO for Jasper image
 */

@Getter
@Setter
@Builder(toBuilder = true)
public class JasperDto {
    String description;
    String dateRange;
    String note;
    Double price;
}
