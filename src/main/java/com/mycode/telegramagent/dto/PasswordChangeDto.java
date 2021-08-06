package com.mycode.telegramagent.dto;

import lombok.*;



/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote This DTO for change password
 */

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeDto {
    private String oldPass;
    private String newPass;
}
