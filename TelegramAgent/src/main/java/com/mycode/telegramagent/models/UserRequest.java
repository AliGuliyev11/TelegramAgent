package com.mycode.telegramagent.models;

import com.mycode.telegramagent.enums.AgentRequestStatus;
import com.mycode.telegramagent.enums.Languages;
import com.mycode.telegramagent.enums.RequestStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@Entity(name = "user_request")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    LocalDateTime expiredDate;
    @Enumerated(EnumType.STRING)
    RequestStatus requestStatus;
    @Enumerated(EnumType.STRING)
    AgentRequestStatus agentRequestStatus;
    @ManyToOne(cascade = CascadeType.PERSIST)
    Agent agent;

}
