package com.mycode.telegramagent.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote This DTO for sending offer to user
 */

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
        return "RabbitOffer{" +
                "offerId=" + offerId +
                ", userId='" + userId + '\'' +
                ", file=" + file +
                '}';
    }
}
