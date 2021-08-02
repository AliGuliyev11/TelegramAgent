package com.mycode.telegramagent.dao.Interface;

import com.mycode.telegramagent.dto.AgentDto;
import com.mycode.telegramagent.models.Agent;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public interface AgentDAO {

    Agent signup(AgentDto agentDto);

    Agent save(Agent agent);

    Agent getAgentByHashCode(int agencyName);

    Boolean checkAgencyName(String agencyName);

    Boolean checkCompanyName(String company);

    Boolean checkEmail(String email);

    Agent getAgentByEmail(String email);

    void removeAgent(Agent agent);
}
