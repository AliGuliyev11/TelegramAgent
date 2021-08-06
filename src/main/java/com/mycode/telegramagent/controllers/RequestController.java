package com.mycode.telegramagent.controllers;

import com.mycode.telegramagent.dto.AgentDto;
import com.mycode.telegramagent.models.UserRequest;
import com.mycode.telegramagent.services.Interface.IOrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ali Guliyev
 * @version 1.0
 * */

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

    @GetMapping("/show-new")
    public List<UserRequest> getAllNewRequests(@RequestAttribute("user") AgentDto agentDto){
        return service.getAllNewRequests(agentDto.getEmail());
    }

    @GetMapping("/show-offer-made")
    public List<UserRequest> getAllOfferMadeRequests(@RequestAttribute("user") AgentDto agentDto){
        return service.getAllOfferMadeRequests(agentDto.getEmail());
    }

    @GetMapping("/show-accepted")
    public List<UserRequest> getAllAcceptedRequests(@RequestAttribute("user") AgentDto agentDto){
        return service.getAllAcceptedRequests(agentDto.getEmail());
    }

    @PutMapping("/add-archive/{id}")
    public UserRequest addToArchive(@PathVariable("id") Long id,@RequestAttribute("user") AgentDto agentDto){
        return service.addToArchive(agentDto.getEmail(),id);
    }

    @GetMapping("/show-archive")
    public List<UserRequest> getAllArchive(@RequestAttribute("user") AgentDto agentDto){
        return service.getAllArchive(agentDto.getEmail());
    }

    @PutMapping("/move-from-archive/{id}")
    public UserRequest moveFromArchive(@PathVariable("id") Long id,@RequestAttribute("user") AgentDto agentDto){
        return service.moveFromArchive(agentDto.getEmail(),id);
    }



}
