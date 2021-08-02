package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class VoenValidation extends RuntimeException{
    public VoenValidation() {
        super("VOEN must contain 10 number");
    }
}
