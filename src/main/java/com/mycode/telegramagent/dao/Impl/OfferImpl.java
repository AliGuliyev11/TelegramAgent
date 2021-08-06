package com.mycode.telegramagent.dao.Impl;

import com.mycode.telegramagent.dao.Interface.OfferDAO;
import com.mycode.telegramagent.dto.*;
import com.mycode.telegramagent.enums.AgentRequestStatus;
import com.mycode.telegramagent.exceptions.NotAnyOffer;
import com.mycode.telegramagent.models.Agent;
import com.mycode.telegramagent.models.Offer;
import com.mycode.telegramagent.models.UserRequest;
import com.mycode.telegramagent.rabbitmq.offerOrder.publisher.RabbitOfferService;
import com.mycode.telegramagent.repositories.AgentRepo;
import com.mycode.telegramagent.repositories.JasperMessageRepo;
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
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static com.mycode.telegramagent.utils.ExpiredDateGenerator.getExpiredDate;
import static com.mycode.telegramagent.utils.GetMessages.getJasperMessage;
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
    private final JasperMessageRepo jasperMessageRepo;

    public OfferImpl(OfferRepo offerRepo, RabbitOfferService offerService, AgentRepo agentRepo,
                     OrderRepo userRequest, LocaleMessageService messageService, JasperMessageRepo jasperMessageRepo) {
        this.offerRepo = offerRepo;
        this.offerService = offerService;
        this.agentRepo = agentRepo;
        this.userRequest = userRequest;
        this.messageService = messageService;
        this.jasperMessageRepo = jasperMessageRepo;
    }

    @Value("${jasper.path}")
    String location;
    @Value("${jasper.file}")
    String resourceFile;

    @Value("${work.begin.time}")
    String beginTime;
    @Value("${work.end.time}")
    String endTime;
    @Value("${expired.time}")
    int expiredTime;
    @Value("${working.days}")
    String[] workingDays;

    /**
     * This request for save and send offer
     *
     * @param uuid     bot user id
     * @param email    agent email
     * @param offerDto current offer by agent
     * @return Offer
     * @apiNote First, program get agent and user request
     * then set expired date which properties goes from application.properties.Convert text to Jasper image and send user
     * Need transactional for getting user request because user request has @Lob column
     */

    @SneakyThrows
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Offer saveOffer(String uuid, String email, OfferDto offerDto) {

        LocalDateTime offerMadeExpiredTime = userRequest.getUserRequestLocalDatetimeByUserID(uuid);

        Agent agent = agentRepo.getAgentByEmail(email);
        UserRequest order = userRequest.getOrderByUserId(uuid, email);
        order.setAgentRequestStatus(AgentRequestStatus.Offer_Made);
        order.setExpiredDate(offerMadeExpiredTime != null ? offerMadeExpiredTime : getExpiredDate(beginTime, endTime, expiredTime, workingDays));
        JSONObject jsonObject = new JSONObject(order.getUserRequest());
        JasperDto jasperDto = offerToJasper(offerDto);
        textToImage(jasperDto, jsonObject.getString("lang"), messageService, location, resourceFile, jasperMessageRepo);
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


    /**
     * This method for when user accept offer change request status and add user data to offer
     * Need transactional for getting user request because user request has @Lob column
     *
     * @param replyToOffer DTO which comes from user
     */


    @Override
    @Transactional
    public void offerAccepted(ReplyToOffer replyToOffer) {
        Offer offer = offerRepo.findById(replyToOffer.getOfferId()).orElse(null);
        if (offer != null) {
            UserRequest userRequest = offer.getUserRequest();
            if (userRequest.getAgentRequestStatus().equals(AgentRequestStatus.Expired)) {
                JSONObject jsonObject = new JSONObject(userRequest.getUserRequest());
                String language = jsonObject.getString("lang");
                offerService.warn(WarningDto.builder()
                        .text(getJasperMessage("warning.repeat", language, jasperMessageRepo, messageService))
                        .userId(userRequest.getUserId()).build());
            } else {
                userRequest.setAgentRequestStatus(AgentRequestStatus.Accepted);
                offer.setAcceptedDate(new Date());
                offer.setPhoneNumber(replyToOffer.getPhoneNumber());
                offer.setUserRequest(userRequest);
                offerRepo.save(offer);
            }
        }


    }

    /**
     * This method for get logged in user request
     *
     * @param uuid  user id
     * @param email current agent email
     * @return UserRequest
     */

    @Override
    public UserRequest getRequestByUUIDAndEmail(String uuid, String email) {
        return userRequest.getOrderByUserId(uuid, email);
    }

    /**
     * This method for get logged in user offers
     *
     * @param email current agent email
     * @return List<Offer>
     */

    @Override
    @Transactional
    public List<Offer> getAgentOffers(String email) {
        return offerRepo.getOffersByAgent_Email(email);
    }

    /**
     * This method for get logged in user offer
     *
     * @param email current agent email
     * @param id    offer id
     * @return Offer
     */

    @Override
    @Transactional
    public Offer getOfferById(String email, Long id) {
        return offerRepo.getOfferByEmailAndId(email, id);
    }
}
