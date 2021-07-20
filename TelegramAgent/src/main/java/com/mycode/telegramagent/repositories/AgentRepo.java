package com.mycode.telegramagent.repositories;

import com.mycode.telegramagent.models.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRepo extends JpaRepository<Agent,Long> {
    Agent getAgentByHashCode(int agencyName);
}
