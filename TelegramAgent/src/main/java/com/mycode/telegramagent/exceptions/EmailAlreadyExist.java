package com.mycode.telegramagent.exceptions;

public class EmailAlreadyExist extends RuntimeException{
    public EmailAlreadyExist() {
        super("Email already exist");
    }
}
