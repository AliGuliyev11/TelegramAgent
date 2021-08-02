package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class EmailValidation extends RuntimeException{
    public EmailValidation() {
        super("Not a correct email format");
    }
}
