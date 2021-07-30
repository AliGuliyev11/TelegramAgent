package com.mycode.telegramagent.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity(name = "jasper_message")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JasperMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String keyword;
    @Column(length = 2500)
    String message;
}