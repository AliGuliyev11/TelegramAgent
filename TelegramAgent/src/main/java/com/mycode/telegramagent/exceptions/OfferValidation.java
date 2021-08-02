package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class OfferValidation extends RuntimeException{
    public OfferValidation() {
        super("Please,fill out all of the info");
    }
}
