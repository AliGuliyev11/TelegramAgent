package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class YouAlreadyMakeOffer extends RuntimeException{
    public YouAlreadyMakeOffer() {
        super("You already make offer to this request");
    }
}
