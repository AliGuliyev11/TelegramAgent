package com.mycode.telegramagent.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String username;
    private String phoneNumber;
}
