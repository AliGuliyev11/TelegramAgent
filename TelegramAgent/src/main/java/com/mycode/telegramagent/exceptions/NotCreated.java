package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class NotCreated extends RuntimeException{
    public NotCreated() {
        super("Agent not created");
    }
}
