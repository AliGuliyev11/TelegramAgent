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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.mycode.telegramagent.utils.Validation.checkOfferMadaInWorkingHours;
import static com.mycode.telegramagent.utils.Validation.validation;

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
        validation(offerDto,jsonObject.getInt("Orderbudget"));
        checkStartDate(offerDto.getStartDate(), jsonObject.getString("Orderdate"));

        return offerDAO.saveOffer(userId, email, offerDto);
    }

    @Override
    public void offerAccepted(ReplyToOffer replyToOffer) {
        offerDAO.offerAccepted(replyToOffer);
    }

    @SneakyThrows
    public void checkStartDate(String startDate, String orderDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start = simpleDateFormat.parse(startDate);
        Date end = simpleDateFormat.parse(orderDate);
        if (start.before(end) && !orderDate.equals(startDate)) {
            throw new CheckStartDate("Your start date must be equal or after " + simpleDateFormat.format(end));
        }
    }

    @Override
    public List<Offer> getAgentOffers(String email) {
        List<Offer> offers = offerDAO.getAgentOffers(email);
        if (offers.isEmpty()) {
            throw new NotAnyOffer();
        }

        return offers;
    }

    @Override
    public Offer getOfferById(Long id, String email) {
        Offer offer=offerDAO.getOfferById(email, id);
        if (offer==null){
            throw new NotAnyOffer();
        }
        return offer;
    }
}
