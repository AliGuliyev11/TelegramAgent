package com.mycode.telegramagent.exceptions;

public class NotAnyOffer extends RuntimeException{
    public NotAnyOffer() {
        super("You haven't any offer.");
    }
}
