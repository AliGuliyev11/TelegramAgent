package com.mycode.telegramagent.services.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycode.telegramagent.dao.Interface.AgentDAO;
import com.mycode.telegramagent.dto.AgentDto;
import com.mycode.telegramagent.dto.PasswordChangeDto;
import com.mycode.telegramagent.enums.RolePriority;
import com.mycode.telegramagent.exceptions.*;
import com.mycode.telegramagent.models.Agent;
import com.mycode.telegramagent.models.Role;
import com.mycode.telegramagent.repositories.RoleRepo;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;
import javax.ws.rs.NotFoundException;
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
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;


    private static final String EMAIL_REGEX = "([a-zA-Z0-9_.+-])+\\@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})";
    private static final String PHONE_REGEX = "[+]{1}[9]{2}[4]{1}(([5]([0]|[1]|[5]))|([7]([0]|[7]))|([9]([9])))[1-9][0-9]{6}";
    private static final String PASSWORD_REGEX = ".{8,}";
    private static final String VOEN_REGEX = "[0-9]{10}";

    public IAgentServiceImpl(Environment environment, AgentDAO agentDAO, EmailServiceImpl emailService, ObjectMapper objectMapper,
                             PasswordEncoder passwordEncoder,RoleRepo roleRepo) {
        this.environment = environment;
        this.agentDAO = agentDAO;
        this.emailService = emailService;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo=roleRepo;
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
        Agent agent = agentDAO.signup(agentDto);
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
        Agent agent = agentDAO.getAgentByEmail(email);
        if (agent == null) {
            throw new NotFoundException();
        }

        if (!passwordEncoder.matches(passwordChangeDto.getOldPass(),agent.getPassword())) {
            throw new PasswordNotMatched();
        }
        updateUserPassword(email, passwordChangeDto.getNewPass());
    }

    private void updateUserPassword(String email, String newPass) {
        Agent agent = agentDAO.getAgentByEmail(email);
        if (agent == null) {
            throw new NotFoundException();
        }
        agent.setPassword(passwordEncoder.encode(newPass));
        agentDAO.save(agent);
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

    @Override
    public void sendPassword(int agencyName) {

        String password = passwordGenerator();
        String text = "This is your new password:" + password;
        Agent agent = agentDAO.getAgentByHashCode(agencyName);
        updateUserPassword(agent.getEmail(), password);
        emailService.sendSimpleMessage(agent.getEmail(), "Your new password", text);
    }


    @Override
    public Boolean verifyUser(int agencyName) {
        Agent agent = agentDAO.getAgentByHashCode(agencyName);
        agent.setIsVerified(true);
        agent.getRoles().clear();
        Role role=roleRepo.getRoleByRolePriority(RolePriority.Standard);
        agent.getRoles().add(role);
        agentDAO.save(agent);
        return true;
    }

}