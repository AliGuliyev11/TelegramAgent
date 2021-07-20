package com.mycode.telegramagent.controllers;

import com.mycode.telegramagent.dto.AgentDto;
import com.mycode.telegramagent.services.Interface.IAgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    IAgentService agentService;

    public AuthController(IAgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping(path = "signup")
    public ResponseEntity<?> signup(@RequestBody AgentDto agentDto) {
        return ResponseEntity.ok(agentService.signup(agentDto));
    }

    @PostMapping(path = "signin")
    public ResponseEntity<?> signIn(@RequestBody AgentDto agentDto) {
        return ResponseEntity.ok(agentService.signin(agentDto));
    }

    @GetMapping(path = "confirm/{agency}")
    public String confirm(@PathVariable("agency") int agencyName, Model model) {


        model.addAttribute("name",agencyName);
        agentService.verifyUser(agencyName);
        return "confirmation";
    }

}
