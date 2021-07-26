package com.mycode.telegramagent.exceptions;

public class RequestInArchive extends RuntimeException{
    public RequestInArchive() {
        super("Request status is de-active");
    }
}
