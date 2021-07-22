package com.mycode.telegramagent.exceptions;

public class EndDateBeforeStart extends RuntimeException{
    public EndDateBeforeStart() {
        super("Your end date mustn't before start date");
    }
}
