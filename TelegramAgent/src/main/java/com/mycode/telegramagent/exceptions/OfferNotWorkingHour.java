package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class OfferNotWorkingHour extends RuntimeException{
    public OfferNotWorkingHour() {
        super("You can send your offer only working hours.");
    }
}
