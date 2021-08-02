package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class CompanyExist extends RuntimeException{
    public CompanyExist() {
        super("Company already exist");
    }
}
