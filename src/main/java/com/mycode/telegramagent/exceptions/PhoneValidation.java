package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class PhoneValidation extends RuntimeException{
    public PhoneValidation() {
        super("Not a correct phone format");
    }
}
