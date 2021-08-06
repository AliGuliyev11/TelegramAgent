package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class RequestExpired extends RuntimeException{
    public RequestExpired() {
        super("Request expired");
    }
}
