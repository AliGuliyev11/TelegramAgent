package com.mycode.telegramagent.exceptions;

public class PhoneValidation extends RuntimeException{
    public PhoneValidation() {
        super("Not a correct phone format");
    }
}
