package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class AgentNotFound extends RuntimeException{
    public AgentNotFound() {
        super("Agent not found.");
    }
}
