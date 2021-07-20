package com.mycode.telegramagent.exceptions;

public class AgencyExist extends RuntimeException{
    public AgencyExist() {
        super("Agency already exist");
    }
}
