package com.mycode.telegramagent.exceptions;

public class PasswordValidation extends RuntimeException{
    public PasswordValidation() {
        super("Your password must contain minimum 8 character");
    }
}
