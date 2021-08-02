package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class NotAnyRequest extends RuntimeException{
    public NotAnyRequest() {
        super("You haven't any request.");
    }
}
