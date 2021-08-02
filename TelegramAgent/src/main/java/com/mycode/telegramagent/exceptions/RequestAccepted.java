package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class RequestAccepted extends RuntimeException{
    public RequestAccepted() {
        super("Request accepted.");
    }
}
