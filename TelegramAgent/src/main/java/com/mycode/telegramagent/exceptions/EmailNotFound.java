package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class EmailNotFound extends RuntimeException{
    public EmailNotFound() {
        super("Email not found");
    }
}
