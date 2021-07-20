package com.mycode.telegramagent.exceptions;

public class CompanyExist extends RuntimeException{
    public CompanyExist() {
        super("Company already exist");
    }
}
