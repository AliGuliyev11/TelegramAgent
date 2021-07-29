package com.mycode.telegramagent.dao.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycode.telegramagent.dao.Interface.AgentDAO;
import com.mycode.telegramagent.dto.AgentDto;
import com.mycode.telegramagent.models.Agent;
import com.mycode.telegramagent.repositories.AgentRepo;
import org.springframework.stereotype.Component;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

@Component
public class AgentImpl implements AgentDAO {

    private AgentRepo agentRepo;
    private ObjectMapper objectMapper;

    public AgentImpl(AgentRepo agentRepo, ObjectMapper objectMapper) {
        this.agentRepo = agentRepo;
        this.objectMapper = objectMapper;
    }

    /**
     * DAO layer sign up
     *
     * @param agentDto Request body
     * @return Agent
     */

    @Override
    public Agent signup(AgentDto agentDto) {

        Agent agent = objectMapper.convertValue(agentDto, Agent.class);
        String hash = String.valueOf(agent.getAgencyName().hashCode()).replaceAll("-", "");
        agent.setHashCode(Integer.valueOf(hash));
        agent.setIsVerified(false);
        agentRepo.save(agent);
        return agent;
    }

    /** This method for save Agent
     * @param agent saved agent
     * @return Agent*/

    @Override
    public Agent save(Agent agent) {
        return agentRepo.save(agent);
    }

    /** This method for getting agent by hash code
     * @param agencyName logged in agency name
     * @return Agent*/

    @Override
    public Agent getAgentByHashCode(int agencyName) {
        return agentRepo.getAgentByHashCode(agencyName);
    }

    /** This method checks agency name is unique or not
     * @param agencyName current agency name
     * @return Boolean*/

    @Override
    public Boolean checkAgencyName(String agencyName) {
        return agentRepo.checkAgency(agencyName);
    }

    @Override
    public Boolean checkCompanyName(String company) {
        return agentRepo.checkCompany(company);
    }

    @Override
    public Boolean checkEmail(String email) {
        return agentRepo.checkEmail(email);
    }

    @Override
    public Agent getAgentByEmail(String email) {
        return agentRepo.getAgentByEmail(email);
    }
}
