package com.mycode.telegramagent.exceptions;

public class NotCreated extends RuntimeException{
    public NotCreated() {
        super("Agent not created");
    }
}
