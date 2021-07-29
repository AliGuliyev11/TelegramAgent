package com.mycode.telegramagent.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;



@Getter
@Setter
public class ReplyToOffer implements Serializable {
    Long offerId;
    String phoneNumber;

    @Override
    public String toString() {
        return "ReplyToOffer{" +
                "offerId=" + offerId +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
