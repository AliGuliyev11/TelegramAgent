package com.mycode.telegramagent.exceptions;


/**
 * @author Ali Guliyev
 * @version 1.0
 */

public class OfferPriceZero extends RuntimeException{
    public OfferPriceZero() {
        super("Offer price mustn't be zero or minus value");
    }
}
