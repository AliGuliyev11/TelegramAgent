package com.mycode.telegramagent.controllers;

import com.mycode.telegramagent.dto.AgentDto;
import com.mycode.telegramagent.dto.ForgotPass;
import com.mycode.telegramagent.services.Interface.IAgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

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


    @PostMapping(path = "forgot-pass")
    public void forgotPass(@RequestBody ForgotPass forgotPass) {
        agentService.forgotPassword(forgotPass.getEmail());
    }

    @GetMapping(path = "confirm/{agency}")
    public String confirm(@PathVariable("agency") int agencyName, Model model) {
        model.addAttribute("name", agencyName);
        return agentService.verifyUser(agencyName);
    }

    @GetMapping(path = "confirmed/{agency}")
    public String sendPassword(@PathVariable("agency") int agencyName) {
        agentService.sendPassword(agencyName);
        return "confirmed and sended";
    }


}
