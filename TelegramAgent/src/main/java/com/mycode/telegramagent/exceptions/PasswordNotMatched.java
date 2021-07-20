package com.mycode.telegramagent.exceptions;

public class PasswordNotMatched extends RuntimeException{
    public PasswordNotMatched() {
        super("Password not matched.");
    }
}
