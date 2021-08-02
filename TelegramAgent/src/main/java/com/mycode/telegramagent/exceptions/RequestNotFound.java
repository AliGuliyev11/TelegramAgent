package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class RequestNotFound extends RuntimeException{
    public RequestNotFound() {
        super("Request not found");
    }
}
