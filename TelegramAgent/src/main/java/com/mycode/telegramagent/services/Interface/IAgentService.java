package com.mycode.telegramagent.services.Interface;

import com.mycode.telegramagent.dto.AgentDto;
import com.mycode.telegramagent.dto.PasswordChangeDto;
import com.mycode.telegramagent.models.Agent;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public interface IAgentService {
    Agent signup(AgentDto agentDto);

    String verifyUser(int agencyName);

    void changePassword(String email, PasswordChangeDto passwordChangeDto);

    void forgotPassword(String email);


    void sendPassword(int agencyName);
}