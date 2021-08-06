package com.mycode.telegramagent.services.Impl;

import com.mycode.telegramagent.dao.Interface.OfferDAO;
import com.mycode.telegramagent.dto.OfferDto;
import com.mycode.telegramagent.dto.ReplyToOffer;
import com.mycode.telegramagent.enums.AgentRequestStatus;
import com.mycode.telegramagent.enums.RequestStatus;
import com.mycode.telegramagent.exceptions.*;
import com.mycode.telegramagent.models.Offer;
import com.mycode.telegramagent.models.UserRequest;
import com.mycode.telegramagent.services.Interface.IOfferService;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mycode.telegramagent.utils.Validation.*;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

@Service
public class OfferServiceImpl implements IOfferService {


    private final OfferDAO offerDAO;

    public OfferServiceImpl(OfferDAO offerDAO) {
        this.offerDAO = offerDAO;
    }

    @Value("${work.begin.time}")
    String beginTime;
    @Value("${work.end.time}")
    String endTime;
    @Value("${working.days}")
    String[] workingDays;

    /**
     * This request for save and send offer
     *
     * @param userId   bot user id
     * @param email    agent email
     * @param offerDto current offer by agent
     * @return Offer
     * @throws YouAlreadyMakeOffer when agent request status is offer made
     * @throws RequestExpired      when agent request status is expired
     * @throws RequestInArchive    when request status is de_active
     * @apiNote First, program get agent and user request
     * then set expired date which properties goes from application.properties.Convert text to Jasper image and send user
     * Need transactional for getting user request because user request has @Lob column
     */

    @SneakyThrows
    @Override
    @Transactional
    public Offer saveOffer(String userId, String email, OfferDto offerDto) {
        UserRequest userRequest = offerDAO.getRequestByUUIDAndEmail(userId, email);

        checkOfferMadaInWorkingHours(beginTime, endTime, workingDays);
        if (userRequest == null) {
            throw new RequestNotFound();
        } else if (userRequest.getAgentRequestStatus().equals(AgentRequestStatus.Offer_Made) ||
                userRequest.getAgentRequestStatus().equals(AgentRequestStatus.Accepted)) {
            throw new YouAlreadyMakeOffer();
        } else if (userRequest.getAgentRequestStatus().equals(AgentRequestStatus.Expired)) {
            throw new RequestExpired();
        } else if (userRequest.getRequestStatus().equals(RequestStatus.De_Active)) {
            throw new RequestInArchive();
        }
        JSONObject jsonObject = new JSONObject(userRequest.getUserRequest());
        System.out.println("aa");
        validation(offerDto, jsonObject.getInt("Orderbudget"));
        System.out.println("bb");
        checkStartDate(offerDto.getStartDate(), jsonObject.getString("Orderdate"), jsonObject.getLong("Orderdateto"));
        checkEndDate(offerDto.getEndDate(), jsonObject.getString("Orderdate"), jsonObject.getLong("Orderdateto"));
        return offerDAO.saveOffer(userId, email, offerDto);
    }


    /**
     * This method for when user accept offer change request status and add user data to offer
     * Need transactional for getting user request because user request has @Lob column
     *
     * @param replyToOffer DTO which comes from user
     */

    @Override
    public void offerAccepted(ReplyToOffer replyToOffer) {
        offerDAO.offerAccepted(replyToOffer);
    }


    /**
     * This method for get logged in user offers
     *
     * @param email current agent email
     * @return List<Offer>
     * @throws NotAnyOffer if not found any offer
     */

    @Override
    public List<Offer> getAgentOffers(String email) {
        List<Offer> offers = offerDAO.getAgentOffers(email);
        if (offers.isEmpty()) {
            throw new NotAnyOffer();
        }

        return offers;
    }

    /**
     * This method for get logged in user offer
     *
     * @param email current agent email
     * @param id    offer id
     * @return Offer
     * @throws NotAnyOffer if not found any offer
     */

    @Override
    public Offer getOfferById(Long id, String email) {
        Offer offer = offerDAO.getOfferById(email, id);
        if (offer == null) {
            throw new NotAnyOffer();
        }
        return offer;
    }
}
