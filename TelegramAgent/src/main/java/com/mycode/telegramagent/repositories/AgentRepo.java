package com.mycode.telegramagent.repositories;

import com.mycode.telegramagent.models.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}
