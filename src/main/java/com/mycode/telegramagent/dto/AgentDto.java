package com.mycode.telegramagent.dto;

import lombok.*;

/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote This DTO for sign up
 */

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AgentDto {
    private String email;
    private String password;
    private String repass;
    private String voen;
    private String agencyName;
    private String companyName;
    private String phoneNumber;
}
