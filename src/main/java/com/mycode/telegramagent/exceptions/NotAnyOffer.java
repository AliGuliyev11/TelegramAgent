package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class NotAnyOffer extends RuntimeException{
    public NotAnyOffer() {
        super("You haven't any offer.");
    }
}
