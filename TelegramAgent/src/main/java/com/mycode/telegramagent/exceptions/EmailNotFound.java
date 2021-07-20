package com.mycode.telegramagent.exceptions;

public class EmailNotFound extends RuntimeException{
    public EmailNotFound() {
        super("Email not found");
    }
}
