package com.mycode.telegramagent.controllers;

import com.mycode.telegramagent.dto.AgentDto;
import com.mycode.telegramagent.services.Interface.IAgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    IAgentService IAgentService;

    public AuthController(IAgentService IAgentService) {
        this.IAgentService = IAgentService;
    }

    @PostMapping(path = "signup")
    public ResponseEntity<?> signup(@RequestBody AgentDto agentDto) {
        return ResponseEntity.ok(IAgentService.signup(agentDto));
    }

    @PostMapping(path = "signin")
    public ResponseEntity<?> signIn(@RequestBody AgentDto agentDto) {
        return ResponseEntity.ok(IAgentService.signin(agentDto));
    }

}
