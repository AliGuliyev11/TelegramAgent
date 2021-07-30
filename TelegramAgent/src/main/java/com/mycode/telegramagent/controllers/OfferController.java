package com.mycode.telegramagent.controllers;

import com.mycode.telegramagent.dto.AgentDto;
import com.mycode.telegramagent.dto.OfferDto;
import com.mycode.telegramagent.models.Offer;
import com.mycode.telegramagent.services.Interface.IOfferService;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Ali Guliyev
 * @version 1.0
 * */

@RestController
@RequestMapping(path = "api/v1/offer")
public class OfferController {


    private final IOfferService offerService;

    public OfferController(IOfferService offerService) {
        this.offerService = offerService;
    }


    @SneakyThrows
    @PostMapping("/{userId}")
    public Offer sendOffer(@RequestAttribute("user") AgentDto agentDto, @PathVariable("userId") String userId, @RequestBody OfferDto offerDto) {

        return offerService.saveOffer(userId, agentDto.getEmail(), offerDto);
    }

    @GetMapping("/show-offers")
    public List<Offer> getAgentOffers(@RequestAttribute("user") AgentDto agentDto) {
        return offerService.getAgentOffers(agentDto.getEmail());
    }
    @GetMapping("/show-offer-detail/{offerId}")
    public Offer getAgentOfferDetail(@PathVariable("offerId") Long offerId,@RequestAttribute("user") AgentDto agentDto) {
        return offerService.getOfferById(offerId,agentDto.getEmail());
    }
}
