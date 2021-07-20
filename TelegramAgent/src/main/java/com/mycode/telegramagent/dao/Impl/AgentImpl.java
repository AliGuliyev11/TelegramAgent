package com.mycode.telegramagent.dao.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycode.telegramagent.dao.Interface.AgentDAO;
import com.mycode.telegramagent.dto.AgentDto;
import com.mycode.telegramagent.models.Agent;
import com.mycode.telegramagent.repositories.AgentRepo;
import org.springframework.stereotype.Component;

@Component
public class AgentImpl implements AgentDAO {

    private AgentRepo agentRepo;
    private ObjectMapper objectMapper;

    public AgentImpl(AgentRepo agentRepo, ObjectMapper objectMapper) {
        this.agentRepo = agentRepo;
        this.objectMapper = objectMapper;
    }

    @Override
    public Agent signup(AgentDto agentDto) {

        Agent agent=objectMapper.convertValue(agentDto,Agent.class);
        String hash=String.valueOf(agent.getAgencyName().hashCode()).replaceAll("-","");
        agent.setHashCode(Integer.valueOf(hash));
        agentRepo.save(agent);
        return agent;
    }

    @Override
    public Agent getAgentByHashCode(int agencyName) {
        return agentRepo.getAgentByHashCode(agencyName);
    }
}
