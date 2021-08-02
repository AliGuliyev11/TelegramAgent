package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class EmailNotVerified extends RuntimeException{
    public EmailNotVerified() {
        super("Email not verified");
    }
}
