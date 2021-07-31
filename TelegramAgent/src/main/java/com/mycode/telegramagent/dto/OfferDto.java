package com.mycode.telegramagent.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote This DTO for sending offer
 */

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
