package com.mycode.telegramagent.exceptions;

public class OfferValidation extends RuntimeException{
    public OfferValidation() {
        super("Please,fill out all of the info");
    }
}
