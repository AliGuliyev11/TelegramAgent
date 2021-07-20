package com.mycode.telegramagent.models;

import com.mycode.telegramagent.services.LifeCycle.AgentLifeCycle;
import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@EntityListeners(AgentLifeCycle.class)
@NoArgsConstructor
@AllArgsConstructor
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String voen;
    private String agencyName;
    private String companyName;
    private String phoneNumber;
    private int hashCode;
}
