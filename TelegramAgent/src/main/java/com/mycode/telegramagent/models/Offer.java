package com.mycode.telegramagent.models;


import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;

@Getter
@Setter
@Entity(name = "offer")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Offer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String userId;
    File file;
    @Column(nullable = false)
    String agencyName;
    @Column(nullable = false)
    String agencyNumber;
}
