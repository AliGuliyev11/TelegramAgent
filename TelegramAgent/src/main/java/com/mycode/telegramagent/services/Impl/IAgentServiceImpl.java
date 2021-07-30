package com.mycode.telegramagent.services.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycode.telegramagent.dao.Interface.AgentDAO;
import com.mycode.telegramagent.dto.AgentDto;
import com.mycode.telegramagent.dto.PasswordChangeDto;
import com.mycode.telegramagent.exceptions.*;
import com.mycode.telegramagent.models.Agent;
import com.mycode.telegramagent.services.Interface.IAgentService;
import com.mycode.telegramagent.services.email.EmailServiceImpl;
import lombok.SneakyThrows;
import org.apache.commons.text.RandomStringGenerator;
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

import javax.validation.constraints.Email;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mycode.telegramagent.utils.PasswordCreator.passwordGenerator;

@Service
public class IAgentServiceImpl implements IAgentService {
    Environment environment;
    AgentDAO agentDAO;
    EmailServiceImpl emailService;
    ObjectMapper objectMapper;

    private static final String EMAIL_REGEX = "([a-zA-Z0-9_.+-])+\\@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})";
    private static final String PHONE_REGEX = "[+]{1}[9]{2}[4]{1}(([5]([0]|[1]|[5]))|([7]([0]|[7]))|([9]([9])))[1-9][0-9]{6}";
    private static final String PASSWORD_REGEX = ".{8,}";
    private static final String VOEN_REGEX = "[0-9]{10}";

    public IAgentServiceImpl(Environment environment, AgentDAO agentDAO, EmailServiceImpl emailService, ObjectMapper objectMapper) {
        this.environment = environment;
        this.agentDAO = agentDAO;
        this.emailService = emailService;
        this.objectMapper = objectMapper;
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
        regexValidation(agentDto);
        checkUnique(agentDto);
        Keycloak keycloak = connectKeycloak();
        UserRepresentation userRepresentation = userRepresentation(agentDto);
        RealmResource realmResource = keycloak.realm(realm);
        RolesResource rolesResource = realmResource.roles();
        UsersResource usersResource = realmResource.users();
        Response response = usersResource.create(userRepresentation);
        if (response.getStatus() == 201) {
            String userId = CreatedResponseUtil.getCreatedId(response);
            CredentialRepresentation passwordCred = passwordCred(agentDto);
            passwordCred.setTemporary(false);

            UserResource userResource = usersResource.get(userId);

            RoleRepresentation realmRoleUser = rolesResource.get(initialRole).toRepresentation();
            userResource.roles().realmLevel().add(Collections.singletonList(realmRoleUser));
            userResource.resetPassword(passwordCred);
            Agent agent = agentDAO.signup(agentDto);
        }

        return agentDto;
    }

    private void regexValidation(AgentDto agentDto) {
        if (!isMatchedRegex(agentDto.getEmail(), EMAIL_REGEX)) {
            throw new EmailValidation();
        } else if (!isMatchedRegex(agentDto.getPhoneNumber(), PHONE_REGEX)) {
            throw new PhoneValidation();
        } else if (!isMatchedRegex(agentDto.getPassword(), PASSWORD_REGEX)) {
            throw new PasswordValidation();
        } else if (!isMatchedRegex(agentDto.getVoen(), VOEN_REGEX)) {
            throw new VoenValidation();
        }
    }


    public static boolean isMatchedRegex(String emailOrNumber, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(emailOrNumber);
        return matcher.matches();
    }

    @Override
    public void changePassword(String email, PasswordChangeDto passwordChangeDto) {

        if (!checkPassword(email, passwordChangeDto.getOldPass())) {
            throw new PasswordNotMatched();
        }
        updateUserPassword(email, passwordChangeDto.getNewPass());
    }

    private void updateUserPassword(String email, String newPass) {
        Keycloak keycloak = connectKeycloak();
        RealmResource realmResource = keycloak.realm(realm);
        UserRepresentation userRep = realmResource.users().search(email).get(0);
        UserResource ur = realmResource.users().get(realmResource.users().search(email).get(0).getId());
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(newPass);
        ur.resetPassword(passwordCred);
        ur.update(userRep);
    }

    @Value("${email.confirmation.url}")
    String emailConfirmationUrl;

    @Override
    public void forgotPassword(String email) {

        Agent agent = agentDAO.getAgentByEmail(email);
        System.out.println(agent);
        if (agent == null) {
            throw new EmailNotFound();
        }
        String url = emailConfirmationUrl + agent.getHashCode();
        String text = "Please,click this link to confirm your email.Then we will send you password.This is confirmation " +
                "link click <a href=" + url + ">here</a>";
        emailService.sendSimpleMessage(email, "Forgot password", text);
    }

    public Boolean checkUnique(AgentDto agentDto) {
        if (agentDAO.checkAgencyName(agentDto.getAgencyName())) {
            throw new AgencyExist();
        } else if (agentDAO.checkCompanyName(agentDto.getCompanyName())) {
            throw new CompanyExist();
        } else if (agentDAO.checkEmail(agentDto.getEmail())) {
            throw new EmailAlreadyExist();
        }
        return true;
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
        UserRepresentation userRep = realmResource.users().search(agentDto.getEmail()).stream().findFirst().orElse(null);
        if (userRep == null) {
            throw new NotCreated();
        }
        if (!userRep.isEmailVerified()) {
            throw new EmailNotVerified();
        }
        if (!checkPassword(agentDto.getEmail(), agentDto.getPassword())) {
            throw new PasswordNotMatched();
        }

        return authzClient.obtainAccessToken(agentDto.getEmail(), agentDto.getPassword());
    }

    public Boolean checkPassword(String email, String password) {
        Map<String, Object> clientCredentials = new HashMap<>();
        clientCredentials.put("secret", clientSecret);
        clientCredentials.put("grant_type", "password");
        Configuration configuration =
                new Configuration(authServerUrl, realm, clientId, clientCredentials, null);
        AuthzClient authzClient = AuthzClient.create(configuration);
        AccessTokenResponse a;
        try {
            a = authzClient.obtainAccessToken(email, password);
        } catch (Exception e) {
            a = null;
        }
        if (a == null) {
            return false;
        }
        return true;
    }

    @Override
    public void sendPassword(int agencyName) {

        String password = passwordGenerator();
        String text = "This is your new password:" + password;
        Agent agent = agentDAO.getAgentByHashCode(agencyName);
        updateUserPassword(agent.getEmail(), password);
        emailService.sendSimpleMessage(agent.getEmail(), "Your new password", text);
    }

    @Override
    public UserRepresentation userRepresentation(AgentDto agentDto) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(agentDto.getEmail());
        user.setEmail(agentDto.getEmail());


        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("phone", Collections.singletonList(agentDto.getPhoneNumber()));
        attributes.put("agencyName", Collections.singletonList(agentDto.getAgencyName()));
        attributes.put("companyName", Collections.singletonList(agentDto.getCompanyName()));

        user.setAttributes(attributes);
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

    @Override
    public Boolean verifyUser(int agencyName) {
        Agent agent = agentDAO.getAgentByHashCode(agencyName);
        String email = agent.getEmail();
        agent.setIsVerified(true);
        agentDAO.save(agent);
        Keycloak keycloak = connectKeycloak();
        RealmResource realmResource = keycloak.realm(realm);
        RolesResource rolesResource = realmResource.roles();
        RoleRepresentation initial = rolesResource.get(initialRole).toRepresentation();
        RoleRepresentation standard = rolesResource.get(standardRole).toRepresentation();
        UserRepresentation userRep = realmResource.users().search(email).get(0);
        userRep.setEmailVerified(true);
        UserResource ur = realmResource.users().get(realmResource.users().search(email).get(0).getId());
        ur.roles().realmLevel().remove(Collections.singletonList(initial));
        ur.roles().realmLevel().add(Collections.singletonList(standard));
        ur.update(userRep);
        return true;
    }


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