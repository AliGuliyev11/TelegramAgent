package com.mycode.telegramagent.exceptions;

public class AgentNotFound extends RuntimeException{
    public AgentNotFound() {
        super("Agent not found.");
    }
}
