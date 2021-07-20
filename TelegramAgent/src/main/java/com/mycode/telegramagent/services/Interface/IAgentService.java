package com.mycode.telegramagent.services.Interface;

import com.mycode.telegramagent.dto.AgentDto;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

public interface IAgentService {
    AgentDto signup(AgentDto agentDto);

    AccessTokenResponse signin(AgentDto agentDto);

    UserRepresentation userRepresentation(AgentDto agentDto);

    CredentialRepresentation passwordCred(AgentDto agentDto);
//
//    Boolean verifyUser(UserDTO userDTO, String token);
//
//    void sendVerifyEmail(String email);

    Keycloak connectKeycloak();
}