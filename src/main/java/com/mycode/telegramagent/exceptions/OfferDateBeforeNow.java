package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class OfferDateBeforeNow extends RuntimeException{
    public OfferDateBeforeNow() {
        super("Your offer must be the present or future tense");
    }
}
