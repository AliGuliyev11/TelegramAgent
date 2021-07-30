package com.mycode.telegramagent.exceptions;

public class AgentValidation extends RuntimeException{
    public AgentValidation() {
        super("Please,fill out all of the info");
    }
}
