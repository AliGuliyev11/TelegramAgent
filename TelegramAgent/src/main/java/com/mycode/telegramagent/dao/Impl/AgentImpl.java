package com.mycode.telegramagent.dao.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycode.telegramagent.dao.Interface.AgentDAO;
import com.mycode.telegramagent.dto.AgentDto;
import com.mycode.telegramagent.enums.RolePriority;
import com.mycode.telegramagent.models.Agent;
import com.mycode.telegramagent.models.Role;
import com.mycode.telegramagent.repositories.AgentRepo;
import com.mycode.telegramagent.repositories.RoleRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

@Component
public class AgentImpl implements AgentDAO, UserDetailsService {

    private final AgentRepo agentRepo;
    private final ObjectMapper objectMapper;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    public AgentImpl(AgentRepo agentRepo, ObjectMapper objectMapper, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.agentRepo = agentRepo;
        this.objectMapper = objectMapper;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * DAO layer sign up
     *
     * @param agentDto Request body
     * @return Agent
     */

    @Override
    public Agent signup(AgentDto agentDto) {

        Agent agent = objectMapper.convertValue(agentDto, Agent.class);
        String hash = String.valueOf(agent.getEmail().hashCode()).replaceAll("-", "");
        agent.setHashCode(Integer.parseInt(hash));
        agent.setIsVerified(false);
        agent.setCreatedDate(LocalDateTime.now());
        agent.setPassword(passwordEncoder.encode(agentDto.getPassword()));
        Role role = roleRepo.getRoleByRolePriority(RolePriority.Initial);
        agent.getRoles().add(role);
        agentRepo.save(agent);
        return agent;
    }

    /** This method for security
     * @apiNote This method for authentication user and give user some roles for authorization
     * @param email user find by email
     * @return UserDetails*/

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Agent agent = agentRepo.getVerifiedAgent(email);
        if (agent == null) {
            throw new UsernameNotFoundException("Agent not found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        agent.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new User(agent.getEmail(), agent.getPassword(), authorities);
    }

    /**
     * This method for save Agent
     *
     * @param agent saved agent
     * @return Agent
     */

    @Override
    public Agent save(Agent agent) {
        return agentRepo.save(agent);
    }

    /**
     * This method for getting agent by hash code
     *
     * @param agencyName logged in agency name
     * @return Agent
     */

    @Override
    public Agent getAgentByHashCode(int agencyName) {
        return agentRepo.getAgentByHashCode(agencyName);
    }

    /**
     * This method checks agency name is unique or not
     *
     * @param agencyName current agency name
     * @return Boolean
     */

    @Override
    public Boolean checkAgencyName(String agencyName) {
        return agentRepo.checkAgency(agencyName);
    }

    /**
     * This method checks company name is unique or not
     *
     * @param company current company name
     * @return Boolean
     */

    @Override
    public Boolean checkCompanyName(String company) {
        return agentRepo.checkCompany(company);
    }

    /**
     * This method checks email is unique or not
     *
     * @param email current agency email
     * @return Boolean
     */

    @Override
    public Boolean checkEmail(String email) {
        return agentRepo.checkEmail(email);
    }

    /**
     * This method for get Agent by email
     *
     * @param email current agency name
     * @return Agent
     */

    @Override
    public Agent getAgentByEmail(String email) {
        return agentRepo.getAgentByEmail(email);
    }

    /**
     * This method for delete Agent from database
     *
     * @param agent deleted agent
     */

    @Override
    public void removeAgent(Agent agent) {
        agentRepo.delete(agent);
    }
}
