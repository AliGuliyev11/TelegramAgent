package com.mycode.telegramagent.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Getter
@Setter
@Builder(toBuilder = true)
public class OfferDto {
    @NotNull
    String description;
    @NotNull
    Date startDate;
    @NotNull
    Date endDate;
    String note;
    @NotNull
    Double price;


}
