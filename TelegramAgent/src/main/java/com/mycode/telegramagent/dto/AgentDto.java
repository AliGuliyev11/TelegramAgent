package com.mycode.telegramagent.dto;

import lombok.*;


@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AgentDto {
    private String email;
    private String password;
    private String voen;
    private String agencyName;
    private String companyName;
    private String phoneNumber;
}
