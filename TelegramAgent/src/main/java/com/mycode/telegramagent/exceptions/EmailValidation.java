package com.mycode.telegramagent.exceptions;

public class EmailValidation extends RuntimeException{
    public EmailValidation() {
        super("Not a correct email format");
    }
}
