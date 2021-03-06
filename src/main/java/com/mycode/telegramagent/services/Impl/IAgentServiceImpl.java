package com.mycode.telegramagent.services.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycode.telegramagent.dao.Interface.AgentDAO;
import com.mycode.telegramagent.dto.AgentDto;
import com.mycode.telegramagent.dto.MailDTO;
import com.mycode.telegramagent.dto.PasswordChangeDto;
import com.mycode.telegramagent.enums.RolePriority;
import com.mycode.telegramagent.exceptions.*;
import com.mycode.telegramagent.models.Agent;
import com.mycode.telegramagent.models.Role;
import com.mycode.telegramagent.repositories.RoleRepo;
import com.mycode.telegramagent.services.Interface.IAgentService;
import com.mycode.telegramagent.services.Locale.LocaleMessageService;
import com.mycode.telegramagent.services.email.EmailServiceImpl;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.mycode.telegramagent.utils.PasswordCreator.passwordGenerator;
import static com.mycode.telegramagent.utils.Validation.regexValidation;
import static com.mycode.telegramagent.utils.Validation.signUpValidation;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

@Service
public class IAgentServiceImpl implements IAgentService {
    Environment environment;
    AgentDAO agentDAO;
    EmailServiceImpl emailService;
    ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;
    LocaleMessageService messageService;


    public IAgentServiceImpl(Environment environment, AgentDAO agentDAO, EmailServiceImpl emailService, ObjectMapper objectMapper,
                             PasswordEncoder passwordEncoder, RoleRepo roleRepo, LocaleMessageService messageService) {
        this.environment = environment;
        this.agentDAO = agentDAO;
        this.emailService = emailService;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
        this.messageService = messageService;
    }

    /**
     * Service layer sign up
     *
     * @param agentDto Request body
     * @return Agent
     */

    @Override
    public Agent signup(AgentDto agentDto) {
        signUpValidation(agentDto);
        regexValidation(agentDto);
        checkUnique(agentDto);
        Agent agent = agentDAO.signup(agentDto);
        return agent;
    }

    /**
     * This method for change logged in agent password
     *
     * @param email             logged in agent email
     * @param passwordChangeDto DTO for password change
     * @throws AgentNotFound
     * @throws PasswordNotMatched
     */

    @SneakyThrows
    @Override
    public void changePassword(String email, PasswordChangeDto passwordChangeDto) {
        Agent agent = agentDAO.getAgentByEmail(email);
        if (agent == null) {
            throw new AgentNotFound();
        }

        if (!passwordEncoder.matches(passwordChangeDto.getOldPass(), agent.getPassword())) {
            throw new PasswordNotMatched();
        }
        updateUserPassword(email, passwordChangeDto.getNewPass());
    }

    /**
     * This method for update password()forgot password condition)
     *
     * @param email   email of agent if exist
     * @param newPass new password of agent
     * @throws AgentNotFound
     */

    @SneakyThrows
    private void updateUserPassword(String email, String newPass) {
        Agent agent = agentDAO.getAgentByEmail(email);
        if (agent == null) {
            throw new AgentNotFound();
        }
        agent.setPassword(passwordEncoder.encode(newPass));
        agentDAO.save(agent);
    }


    @Value("${email.confirmation.url}")
    String emailConfirmationUrl;
    @Value("${forgotpass.template}")
    String forgotPassTemp;
    @Value("${confirmation.template}")
    String confirmationTemp;

    /**
     * This method for forgot password email sending
     *
     * @param email email of agent if exist
     * @throws EmailNotFound
     */

    @Override
    public void forgotPassword(String email) {

        Agent agent = agentDAO.getAgentByEmail(email);
        if (agent == null) {
            throw new AgentNotFound();
        }
        if (agent.getIsVerified()) {
            String url = emailConfirmationUrl + agent.getHashCode();
            String text = messageService.getMessage("agent.forgot.password.verified.text");
            MailDTO mail = MailDTO.builder().to(email).subject(messageService.getMessage("agent.forgot.password.subject"))
                    .buttonName(messageService.getMessage("agent.forgot.password.verified.button.text")).text(text).templateName(confirmationTemp).link(url).build();

            emailService.sendSimpleMessage(mail);
        } else {
            MailDTO mail = MailDTO.builder().to(email).subject(messageService.getMessage("agent.forgot.password.subject"))
                    .text(messageService.getMessage("agent.forgot.password.not-verified.text")).templateName(forgotPassTemp).build();
            emailService.sendSimpleMessage(mail);
        }

    }

    /**
     * This method for check agent DTO some elements is unique or not
     *
     * @param agentDto sign upped agent
     * @return Boolean
     * @throws AgencyExist
     * @throws CompanyExist
     * @throws EmailAlreadyExist
     */

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

    @Value("${passwordsender.template}")
    String passwordTemp;

    /**
     * This method for sending new password to agent
     *
     * @param agencyName agency hash code
     */


    @Override
    public void sendPassword(int agencyName) {

        String password = passwordGenerator();
        Agent agent = agentDAO.getAgentByHashCode(agencyName);
        updateUserPassword(agent.getEmail(), password);
        MailDTO mail = MailDTO.builder().to(agent.getEmail()).subject(messageService.getMessage("agent.send.password.subject"))
                .text(password).templateName(passwordTemp).build();
        emailService.sendSimpleMessage(mail);
    }

    @Value("${agent.email.confirmation.limit}")
    long limitConfirmationEmail;

    @Value("${email.verify.agent.url}")
    String emailUrl;

    /**
     * This method for verify agent
     *
     * @param agencyName agent hash code
     * @return String
     */

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
            String url = emailUrl + agent.getHashCode();
            String text = messageService.getMessage("agent.lifecycle.email.text", limitConfirmationEmail);
            MailDTO mail = MailDTO.builder().to(agent.getEmail()).subject(messageService.getMessage("agent.confirmation.subject"))
                    .buttonName(messageService.getMessage("agent.confirmation.button.text")).text(text).templateName(confirmationTemp).link(url).build();
            agent.setCreatedDate(LocalDateTime.now());
            agentDAO.save(agent);
            emailService.sendSimpleMessage(mail);
            return "email expired we send you another email";
        }

    }

}