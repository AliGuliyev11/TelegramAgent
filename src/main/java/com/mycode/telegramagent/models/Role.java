package com.mycode.telegramagent.models;

import com.mycode.telegramagent.enums.RolePriority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote This class for security role of agent
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private RolePriority rolePriority;
}
