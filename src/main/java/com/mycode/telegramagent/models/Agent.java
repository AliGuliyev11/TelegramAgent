package com.mycode.telegramagent.models;

import com.mycode.telegramagent.services.LifeCycle.AgentLifeCycle;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote This class for Agent entity
 */

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@EntityListeners(AgentLifeCycle.class)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private Boolean isVerified;
    LocalDateTime createdDate;
    private String voen;
    private String agencyName;
    private String password;
    private String companyName;
    private String phoneNumber;
    private int hashCode;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles=new ArrayList<>();

}
