package com.mycode.telegramagent.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote This DTO for sending message for expiration request
 */

@Getter
@Setter
@Builder(toBuilder = true)
public class WarningDto {
    String text;
    String userId;
}
