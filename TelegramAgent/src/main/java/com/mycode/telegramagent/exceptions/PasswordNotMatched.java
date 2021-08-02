package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class PasswordNotMatched extends RuntimeException{
    public PasswordNotMatched() {
        super("Password not matched.");
    }
}
