package com.mycode.telegramagent.exceptions;

public class RequestNotFound extends RuntimeException{
    public RequestNotFound() {
        super("Request not found");
    }
}
