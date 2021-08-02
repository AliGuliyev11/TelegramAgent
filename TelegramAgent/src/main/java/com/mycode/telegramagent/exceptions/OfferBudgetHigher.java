package com.mycode.telegramagent.exceptions;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class OfferBudgetHigher extends RuntimeException{
    public OfferBudgetHigher() {
        super("Offer budget too higher.");
    }
}
