package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class PasswordValidation extends RuntimeException{
    public PasswordValidation() {
        super("Your password must contain minimum 8 character");
    }
}
