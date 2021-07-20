package com.mycode.telegramagent.dto;

import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeDto {
    private String oldPass;
    private String newPass;
}
