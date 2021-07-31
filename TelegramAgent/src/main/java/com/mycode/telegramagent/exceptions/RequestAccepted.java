package com.mycode.telegramagent.exceptions;

public class RequestAccepted extends RuntimeException{
    public RequestAccepted() {
        super("Request accepted.");
    }
}
