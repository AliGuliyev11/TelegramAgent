package com.mycode.telegramagent.exceptions;

public class OfferDateBeforeNow extends RuntimeException{
    public OfferDateBeforeNow() {
        super("Your offer must be the present or future tense");
    }
}
