package com.mycode.telegramagent.services.Impl;

import com.mycode.telegramagent.dto.AgentDto;
import com.mycode.telegramagent.exceptions.EmailNotVerified;
import com.mycode.telegramagent.services.Interface.IAgentService;
import lombok.SneakyThrows;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class IAgentServiceImpl implements IAgentService {
    Environment environment;
//    UserDAO userDAO;
//    VerifyTokenDAO vtDAO;
//    ModelMapperComponent modelMapperComponent;
//    SchedulerExecutorComponent schEx;

    public IAgentServiceImpl(Environment environment) {
        this.environment = environment;
//        this.userDAO = userDAO;
//        this.vtDAO = vtDAO;
//        this.modelMapperComponent = modelMapperComponent;
//        this.schEx = schEx;
    }

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.resource}")
    private String clientId;
    @Value("${keycloak.credentials.secret}")
    private String clientSecret;
    @Value("${app.keycloak.initial.role}")
    private String initialRole;
    @Value("${app.keycloak.standard.role}")
    private String standardRole;

    @Override
    public AgentDto signup(AgentDto agentDto) {
        Keycloak keycloak = connectKeycloak();
        //Call account service to save user
        UserRepresentation userRepresentation = userRepresentation(agentDto);
        //Get realm
        RealmResource realmResource = keycloak.realm(realm);
        RolesResource rolesResource = realmResource.roles();
        UsersResource usersResource = realmResource.users();
        Response response = usersResource.create(userRepresentation);
//        userDTO.setStatusCode(response.getStatus());
//        userDTO.setStatus(response.getStatusInfo().toString());
        if (response.getStatus() == 201) {
            String userId = CreatedResponseUtil.getCreatedId(response);
            // create password credential
            CredentialRepresentation passwordCred = passwordCred(agentDto);
            passwordCred.setTemporary(false);

            UserResource userResource = usersResource.get(userId);

            RoleRepresentation realmRoleUser = rolesResource.get(initialRole).toRepresentation();
            // Assign realm role student to user
            userResource.roles().realmLevel().add(Collections.singletonList(realmRoleUser));
            // Set password credential
            userResource.resetPassword(passwordCred);
            //create newUser for database
//            AppUser appUser = userDAO.createUser(userDTO);
//            sendVerifyEmail(appUser.getEmail());
        }

        return agentDto;
    }

    @SneakyThrows
    @Override
    public AccessTokenResponse signin(AgentDto agentDto) {
        Map<String, Object> clientCredentials = new HashMap<>();
        clientCredentials.put("secret", clientSecret);
        clientCredentials.put("grant_type", "password");
        Configuration configuration =
                new Configuration(authServerUrl, realm, clientId, clientCredentials, null);
        AuthzClient authzClient = AuthzClient.create(configuration);

        Keycloak keycloak = connectKeycloak();
        RealmResource realmResource = keycloak.realm(realm);
        UserRepresentation userRep = realmResource.users().search(agentDto.getEmail()).get(0);
        if (!userRep.isEmailVerified()){
            throw new EmailNotVerified();
        }

        return authzClient.obtainAccessToken(agentDto.getEmail(), agentDto.getPassword());
    }

    @Override
    public UserRepresentation userRepresentation(AgentDto agentDto) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(agentDto.getEmail());
        user.setEmail(agentDto.getEmail());
        user.setAttributes(Collections.singletonMap("phone", Collections.singletonList(agentDto.getPhoneNumber())));
        user.setAttributes(Collections.singletonMap("agencyName", Collections.singletonList(agentDto.getAgencyName())));
        user.setAttributes(Collections.singletonMap("companyName", Collections.singletonList(agentDto.getCompanyName())));
        return user;
    }

    @Override
    public CredentialRepresentation passwordCred(AgentDto agentDto) {
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(agentDto.getPassword());
        return passwordCred;
    }

//    @Override
//    public Boolean verifyUser(UserDTO userDTO, String token) {
//        String email = userDTO.getEmail();
//        VerificationToken vToken = vtDAO.findByToken(token);
//        if (vToken.getEmail().equals(email)) {
//            Keycloak keycloak = connectKeycloak();
//            RealmResource realmResource = keycloak.realm(realm);
//            RolesResource rolesResource = realmResource.roles();
//            RoleRepresentation initial = rolesResource.get(initialRole).toRepresentation();
//            RoleRepresentation standard = rolesResource.get(standardRole).toRepresentation();
//            UserRepresentation userRep = realmResource.users().search(email).get(0);
//            userRep.setEmailVerified(true);
//            UserResource ur = realmResource.users().get(realmResource.users().search(email).get(0).getId());
//            ur.roles().realmLevel().remove(Collections.singletonList(initial));
//            ur.roles().realmLevel().add(Collections.singletonList(standard));
//            ur.update(userRep);
//            vtDAO.delete(vToken);
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void sendVerifyEmail(String email) {
//        AppUser appUser = userDAO.getUserByEmail(email);
//        schEx.runEmailVerification(appUser);
//    }

    @Override
    public Keycloak connectKeycloak() {
        Keycloak keycloak = KeycloakBuilder.builder().serverUrl(environment.getProperty("keycloak.auth-server-url"))
                .grantType(OAuth2Constants.PASSWORD).realm(environment.getProperty("app.keycloak.realmMain"))
                .clientId(environment.getProperty("app.keycloak.clientIdMain")).username(environment.getProperty("app.keycloak.usernameMain"))
                .password(environment.getProperty("app.keycloak.passwordMain"))
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build();
        keycloak.tokenManager().getAccessToken();
        return keycloak;
    }
}