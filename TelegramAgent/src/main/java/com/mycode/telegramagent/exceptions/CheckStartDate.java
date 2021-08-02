package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class CheckStartDate extends RuntimeException{
    public CheckStartDate(String message) {
        super(message);
    }
}
