package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class EndDateBeforeStart extends RuntimeException{
    public EndDateBeforeStart() {
        super("Your end date mustn't before start date");
    }
}
