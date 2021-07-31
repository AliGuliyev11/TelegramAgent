package com.mycode.telegramagent.services.Interface;

import com.mycode.telegramagent.dto.AgentDto;
import com.mycode.telegramagent.dto.PasswordChangeDto;

public interface IAgentService {
    AgentDto signup(AgentDto agentDto);

//    void signin(AgentDto agentDto);

//

    Boolean verifyUser(int agencyName);

    void changePassword(String email, PasswordChangeDto passwordChangeDto);

    void forgotPassword(String email);


    void sendPassword(int agencyName);
}