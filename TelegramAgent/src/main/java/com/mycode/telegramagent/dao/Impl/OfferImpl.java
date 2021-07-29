package com.mycode.telegramagent.dao.Impl;

import com.mycode.telegramagent.dao.Interface.OfferDAO;
import com.mycode.telegramagent.dto.JasperDto;
import com.mycode.telegramagent.dto.OfferDto;
import com.mycode.telegramagent.dto.RabbitOffer;
import com.mycode.telegramagent.dto.ReplyToOffer;
import com.mycode.telegramagent.enums.AgentRequestStatus;
import com.mycode.telegramagent.enums.Languages;
import com.mycode.telegramagent.models.Agent;
import com.mycode.telegramagent.models.Offer;
import com.mycode.telegramagent.models.UserRequest;
import com.mycode.telegramagent.rabbitmq.offerOrder.publisher.RabbitOfferService;
import com.mycode.telegramagent.repositories.AgentRepo;
import com.mycode.telegramagent.repositories.OfferRepo;
import com.mycode.telegramagent.repositories.OrderRepo;
import com.mycode.telegramagent.services.Locale.LocaleMessageService;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;
import java.util.List;

import static com.mycode.telegramagent.utils.OfferToJasper.offerToJasper;
import static com.mycode.telegramagent.utils.TextToImage.textToImage;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

@Component
public class OfferImpl implements OfferDAO {

    private final OfferRepo offerRepo;
    private final RabbitOfferService offerService;
    private final AgentRepo agentRepo;
    private ModelMapper modelMapper = new ModelMapper();
    private final OrderRepo userRequest;
    private final LocaleMessageService messageService;

    public OfferImpl(OfferRepo offerRepo, RabbitOfferService offerService, AgentRepo agentRepo, OrderRepo userRequest,
                     LocaleMessageService messageService) {
        this.offerRepo = offerRepo;
        this.offerService = offerService;
        this.agentRepo = agentRepo;
        this.userRequest = userRequest;
        this.messageService = messageService;
    }

    @Value("${jasper.path}")
    String location;
    @Value("${jasper.file}")
    String resourceFile;


    @SneakyThrows
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Offer saveOffer(String uuid, String email, OfferDto offerDto) {
        Agent agent = agentRepo.getAgentByEmail(email);
        UserRequest order = userRequest.getOrderByUserId(uuid, email);
        order.setAgentRequestStatus(AgentRequestStatus.Offer_Made);
        JSONObject jsonObject = new JSONObject(order.getUserRequest());
        JasperDto jasperDto = offerToJasper(agent.getAgencyName(), offerDto);

        textToImage(jasperDto,Languages.valueOf(jsonObject.getString("lang")), messageService, location, resourceFile);
        File photo = new File(location);

        Offer offer = modelMapper.map(offerDto, Offer.class);
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        offer.setAgent(agent);
        offer.setUserRequest(order);
        Offer savedOffer = offerRepo.save(offer);
        RabbitOffer rabbitOffer = RabbitOffer.builder().userId(uuid).offerId(savedOffer.getId()).file(photo).build();
        offerService.send(rabbitOffer);
        order.setOffer(savedOffer);
        userRequest.save(order);
        return savedOffer;
    }

    @Override
    @Transactional
    public void offerAccepted(ReplyToOffer replyToOffer) {
        Offer offer = offerRepo.findById(replyToOffer.getOfferId()).get();
        offer.setAcceptedDate(new Date());
        offer.setPhoneNumber(replyToOffer.getPhoneNumber());
        UserRequest userRequest = offer.getUserRequest();
        userRequest.setAgentRequestStatus(AgentRequestStatus.Accepted);
        offer.setUserRequest(userRequest);
        offerRepo.save(offer);
    }

    @Override
    public UserRequest getRequestByUUIDAndEmail(String uuid, String email) {
        return userRequest.getOrderByUserId(uuid, email);
    }

    @Override
    public List<Offer> getAgentOffers(String email) {
        return offerRepo.getOffersByAgent_Email(email);
    }


    @Override
    public Offer getOfferById(Long id) {
        return offerRepo.findById(id).get();
    }
}
