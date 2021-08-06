package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class AgentValidation extends RuntimeException{
    public AgentValidation() {
        super("Please,fill out all of the info");
    }
}
