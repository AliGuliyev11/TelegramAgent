package com.mycode.telegramagent.exceptions;

public class VoenValidation extends RuntimeException{
    public VoenValidation() {
        super("VOEN must contain 10 number");
    }
}
