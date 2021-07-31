package com.mycode.telegramagent.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mycode.telegramagent.enums.AgentRequestStatus;
import com.mycode.telegramagent.enums.Languages;
import com.mycode.telegramagent.enums.RequestStatus;
import com.mycode.telegramagent.services.LifeCycle.OrderLifeCycle;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@Entity(name = "user_request")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(OrderLifeCycle.class)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Lob
    @Column
    String userRequest;
    String userId;
    LocalDateTime expiredDate;
    @Enumerated(EnumType.STRING)
    RequestStatus requestStatus;
    @Enumerated(EnumType.STRING)
    AgentRequestStatus agentRequestStatus;
    @ManyToOne
    Agent agent;
    @OneToOne(targetEntity = Offer.class,orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    Offer offer;

}
