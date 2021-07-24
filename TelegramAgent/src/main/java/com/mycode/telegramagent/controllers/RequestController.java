package com.mycode.telegramagent.controllers;

import com.mycode.telegramagent.dto.AgentDto;
import com.mycode.telegramagent.models.UserRequest;
import com.mycode.telegramagent.services.Interface.IOrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/request")
public class RequestController {

    private final IOrderService service;

    public RequestController(IOrderService service) {
        this.service = service;
    }

    @GetMapping("/show-all")
    public List<UserRequest> getAllRequests(@RequestAttribute("user") AgentDto agentDto){
        return service.getAllRequests(agentDto.getEmail());
    }

    @PutMapping("/add-archive/{id}")
    public UserRequest addToArchive(@PathVariable("id") Long id,@RequestAttribute("user") AgentDto agentDto){
        return service.addToArchive(agentDto.getEmail(),id);
    }

    @GetMapping("/show-archive")
    public List<UserRequest> getAllArchive(@RequestAttribute("user") AgentDto agentDto){
        return service.getAllArchive(agentDto.getEmail());
    }





}
