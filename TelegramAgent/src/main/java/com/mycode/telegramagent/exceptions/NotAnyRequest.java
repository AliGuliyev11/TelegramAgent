package com.mycode.telegramagent.exceptions;

public class NotAnyRequest extends RuntimeException{
    public NotAnyRequest() {
        super("You haven't any request.");
    }
}
