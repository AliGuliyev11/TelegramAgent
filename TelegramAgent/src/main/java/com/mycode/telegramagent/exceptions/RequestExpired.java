package com.mycode.telegramagent.exceptions;

public class RequestExpired extends RuntimeException{
    public RequestExpired() {
        super("Request expired");
    }
}
