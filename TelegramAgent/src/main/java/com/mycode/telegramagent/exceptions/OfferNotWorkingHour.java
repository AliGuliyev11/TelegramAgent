package com.mycode.telegramagent.exceptions;

public class OfferNotWorkingHour extends RuntimeException{
    public OfferNotWorkingHour() {
        super("You can send your offer only working hours.");
    }
}
