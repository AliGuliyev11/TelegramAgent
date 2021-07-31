package com.mycode.telegramagent.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Getter
@Setter
@Entity(name = "offer")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Offer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String description;
    Date startDate;
    Date endDate;
    String note;
    Double price;
    @OneToOne(targetEntity = UserRequest.class)
    @JsonManagedReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    UserRequest userRequest;
    @ManyToOne
    Agent agent;
    Date acceptedDate;
    String phoneNumber;
}
