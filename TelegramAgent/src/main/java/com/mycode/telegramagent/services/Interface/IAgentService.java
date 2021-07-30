package com.mycode.telegramagent.services.Interface;

import com.mycode.telegramagent.dto.AgentDto;
import com.mycode.telegramagent.dto.PasswordChangeDto;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

public interface IAgentService {
    AgentDto signup(AgentDto agentDto);

//    void signin(AgentDto agentDto);

//

    Boolean verifyUser(int agencyName);
//
//    void sendVerifyEmail(String email);

    void changePassword(String email, PasswordChangeDto passwordChangeDto);

    void forgotPassword(String email);


    void sendPassword(int agencyName);
}