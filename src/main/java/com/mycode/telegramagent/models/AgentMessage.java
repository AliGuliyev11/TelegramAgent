package com.mycode.telegramagent.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote This class for Jasper messages
 */

@Entity(name = "agent_message")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AgentMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String keyword;
    @Column(length = 2500)
    String message;
}
