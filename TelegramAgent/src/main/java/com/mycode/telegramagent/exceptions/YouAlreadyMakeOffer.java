package com.mycode.telegramagent.exceptions;

public class YouAlreadyMakeOffer extends RuntimeException{
    public YouAlreadyMakeOffer() {
        super("You already make offer to this request");
    }
}
