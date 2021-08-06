package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class EmailAlreadyExist extends RuntimeException{
    public EmailAlreadyExist() {
        super("Email already exist");
    }
}
