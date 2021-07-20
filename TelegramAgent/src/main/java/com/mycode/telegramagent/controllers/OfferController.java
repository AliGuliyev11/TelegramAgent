package com.mycode.telegramagent.controllers;

import com.mycode.telegramagent.dto.AgentDto;
import com.mycode.telegramagent.models.Offer;
import com.mycode.telegramagent.services.Interface.IOfferService;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping(path = "api/v1/offer")
public class OfferController {


    private final IOfferService offerService;

    public OfferController(IOfferService offerService) {
        this.offerService = offerService;
    }


    @GetMapping("/get")
    public String er(@RequestAttribute("user") AgentDto agentDto) {
        System.out.println("qwrqr"+agentDto.getEmail());
        return "Salam";
    }


    @SneakyThrows
    @PostMapping("/{userId}")
    public Offer sendOffer(@PathVariable("userId") String userId, @RequestParam("agencyName") String agencyName,
                           @RequestParam("agencyNumber") String agencyNumber, @RequestParam(value = "File") MultipartFile file) {
        return offerService.saveOffer(userId, agencyName, agencyNumber, file);
    }
}
