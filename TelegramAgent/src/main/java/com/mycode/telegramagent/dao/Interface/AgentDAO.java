package com.mycode.telegramagent.dao.Interface;

import com.mycode.telegramagent.dto.AgentDto;
import com.mycode.telegramagent.models.Agent;

public interface AgentDAO {

    Agent signup(AgentDto agentDto);
    Agent getAgentByHashCode(int agencyName);
}
