package com.mycode.telegramagent.exceptions;

public class OfferPriceZero extends RuntimeException{
    public OfferPriceZero() {
        super("Offer price mustn't be zero or minus value");
    }
}
