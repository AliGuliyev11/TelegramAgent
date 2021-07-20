package com.mycode.telegramagent.controllers;

import com.mycode.telegramagent.dto.AgentDto;
import com.mycode.telegramagent.dto.PasswordChangeDto;
import com.mycode.telegramagent.services.Interface.IAgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/agent")
public class AgentController {

    IAgentService agentService;

    public AgentController(IAgentService agentService) {
        this.agentService = agentService;
    }

    @PutMapping("change-password")
    public void changePassword(@RequestAttribute("user") AgentDto agentDto, @RequestBody PasswordChangeDto password) {
        agentService.changePassword(agentDto.getEmail(), password);
    }
}
