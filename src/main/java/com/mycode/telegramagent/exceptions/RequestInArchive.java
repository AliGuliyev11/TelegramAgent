package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class RequestInArchive extends RuntimeException{
    public RequestInArchive() {
        super("Request status is de-active");
    }
}
