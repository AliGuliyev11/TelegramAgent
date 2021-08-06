package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class AgencyExist extends RuntimeException{
    public AgencyExist() {
        super("Agency already exist");
    }
}
