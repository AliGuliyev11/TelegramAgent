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
import javassist.NotFoundException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mycode.telegramagent.utils.PasswordCreator.passwordGenerator;
import static com.mycode.telegramagent.utils.Validation.signUpValidation;

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
                             PasswordEncoder passwordEncoder, RoleRepo roleRepo) {
        this.environment = environment;
        this.agentDAO = agentDAO;
        this.emailService = emailService;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
    }

    @Override
    public AgentDto signup(AgentDto agentDto) {
        signUpValidation(agentDto);
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
        } else if (agentDto.getVoen()!=null && !isMatchedRegex(agentDto.getVoen(), VOEN_REGEX)) {
            throw new VoenValidation();
        }
    }


    public static boolean isMatchedRegex(String emailOrNumber, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(emailOrNumber);
        return matcher.matches();
    }

    @SneakyThrows
    @Override
    public void changePassword(String email, PasswordChangeDto passwordChangeDto) {
        Agent agent = agentDAO.getAgentByEmail(email);
        if (agent == null) {
            throw new NotFoundException("Agent not found");
        }

        if (!passwordEncoder.matches(passwordChangeDto.getOldPass(), agent.getPassword())) {
            throw new PasswordNotMatched();
        }
        updateUserPassword(email, passwordChangeDto.getNewPass());
    }

    @SneakyThrows
    private void updateUserPassword(String email, String newPass) {
        Agent agent = agentDAO.getAgentByEmail(email);
        if (agent == null) {
            throw new NotFoundException("Agent not found");
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

    @Value("${agent.email.confirmation.limit}")
    long limitConfirmationEmail;

    @Override
    public String verifyUser(int agencyName) {

        Agent agent = agentDAO.getAgentByHashCode(agencyName);
        long minutes = ChronoUnit.MINUTES.between(agent.getCreatedDate(), LocalDateTime.now());
        if (minutes <= limitConfirmationEmail) {
            agent.setIsVerified(true);
            agent.getRoles().clear();
            Role role = roleRepo.getRoleByRolePriority(RolePriority.Standard);
            agent.getRoles().add(role);
            agentDAO.save(agent);
            return "confirmed successfully";
        } else {
            if (agent != null) {
                agentDAO.removeAgent(agent);
            }
            return "email expired";
        }

    }

}