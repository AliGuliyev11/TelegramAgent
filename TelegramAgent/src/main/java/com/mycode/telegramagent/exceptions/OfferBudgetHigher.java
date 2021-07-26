package com.mycode.telegramagent.exceptions;

public class OfferBudgetHigher extends RuntimeException{
    public OfferBudgetHigher() {
        super("Offer budget too higher.");
    }
}
