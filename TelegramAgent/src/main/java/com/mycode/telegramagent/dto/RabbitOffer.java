package com.mycode.telegramagent.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.File;
import java.io.Serializable;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RabbitOffer implements Serializable {
    Long offerId;
    String userId;
    File file;

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + offerId +
                ", userId='" + userId + '\'' +
                ", file=" + file +
                '}';
    }
}
