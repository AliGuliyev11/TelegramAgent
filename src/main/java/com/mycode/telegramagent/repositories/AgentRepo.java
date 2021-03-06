package com.mycode.telegramagent.repositories;

import com.mycode.telegramagent.models.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote Repository of Agent
 */

public interface AgentRepo extends JpaRepository<Agent, Long> {
    Agent getAgentByHashCode(int agencyName);

    @Query(value = "SELECT CASE WHEN EXISTS (SELECT * from agent a where a.agency_name=:agencyName)THEN true ELSE FALSE END",
            nativeQuery = true)
    Boolean checkAgency(String agencyName);

    @Query(value = "SELECT CASE WHEN EXISTS (SELECT * from agent a where a.company_name=:company)THEN true ELSE FALSE END",
            nativeQuery = true)
    Boolean checkCompany(String company);

    @Query(value = "SELECT CASE WHEN EXISTS (SELECT * from agent a where a.email=:email)THEN true ELSE FALSE END",
            nativeQuery = true)
    Boolean checkEmail(String email);

    Agent getAgentByEmail(String email);
    @Query(value = "SELECT * from agent a WHERE a.is_verified=true",nativeQuery = true)
    List<Agent> getVerifiedAgents();
    @Query(value = "SELECT * from agent a WHERE a.is_verified=true AND a.email=:email",nativeQuery = true)
    Agent getVerifiedAgent(String email);

}
