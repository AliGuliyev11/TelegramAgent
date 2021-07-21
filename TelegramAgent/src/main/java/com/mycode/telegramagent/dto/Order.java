package com.mycode.telegramagent.dto;

import com.mycode.telegramagent.enums.Languages;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {

    Long id;
    Languages language;
    String Ordertravel;
    String Orderaddress1;
    String Orderaddress2;
    Date Orderdate;
    int Ordertraveller;
    int Orderbudget;
    int Orderdateto;
    Long chatId;
    String userId;
    Date createdDate;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", language=" + language +
                ", Ordertravel='" + Ordertravel + '\'' +
                ", Orderaddress1='" + Orderaddress1 + '\'' +
                ", Orderaddress2='" + Orderaddress2 + '\'' +
                ", Orderdate=" + Orderdate +
                ", Ordertraveller=" + Ordertraveller +
                ", Orderbudget=" + Orderbudget +
                ", Orderdateto=" + Orderdateto +
                ", chatId=" + chatId +
                ", userId='" + userId + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
