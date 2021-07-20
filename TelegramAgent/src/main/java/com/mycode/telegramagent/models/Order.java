package com.mycode.telegramagent.models;

import com.mycode.telegramagent.enums.Languages;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity(name = "userOrder")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {

    @Id
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
    Date expiredDate;

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
                ", expiredDate=" + expiredDate +
                '}';
    }
}
