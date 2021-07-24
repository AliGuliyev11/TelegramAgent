package com.mycode.telegramagent.services.Impl;

import com.mycode.telegramagent.dao.Interface.OfferDAO;
import com.mycode.telegramagent.dto.OfferDto;
import com.mycode.telegramagent.dto.ReplyToOffer;
import com.mycode.telegramagent.enums.AgentRequestStatus;
import com.mycode.telegramagent.exceptions.*;
import com.mycode.telegramagent.models.Offer;
import com.mycode.telegramagent.models.UserRequest;
import com.mycode.telegramagent.services.Interface.IOfferService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.mycode.telegramagent.utils.Validation.validation;

@Service
public class OfferServiceImpl implements IOfferService {



    private final OfferDAO offerDAO;

    public OfferServiceImpl(OfferDAO offerDAO) {
        this.offerDAO = offerDAO;
    }

    @SneakyThrows
    @Override
    public Offer saveOffer(String userId, String email, OfferDto offerDto) {
        UserRequest userRequest = offerDAO.getRequestByUUIDAndEmail(userId, email);

        if (userRequest == null) {
            throw new RequestNotFound();
        } else if (userRequest.getAgentRequestStatus().equals(AgentRequestStatus.Offer_Made) ||
                userRequest.getAgentRequestStatus().equals(AgentRequestStatus.Accepted)) {
            throw new YouAlreadyMakeOffer();
        } else if (userRequest.getAgentRequestStatus().equals(AgentRequestStatus.Expired)) {
            throw new RequestExpired();
        }
        checkStartDate(offerDto.getStartDate(),userRequest.getOrderdate());
        validation(offerDto);


        return offerDAO.saveOffer(userId, email, offerDto);
    }

    @Override
    public void offerAccepted(ReplyToOffer replyToOffer) {
        offerDAO.offerAccepted(replyToOffer);
    }

    @SneakyThrows
    private void checkStartDate(String startDate, Date orderDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start = simpleDateFormat.parse(startDate);
        String a=simpleDateFormat.format(orderDate);
        Date requestedDate=simpleDateFormat.parse(a);
        if (start.before(requestedDate) && !simpleDateFormat.format(orderDate).equals(startDate)) {
            throw new CheckStartDate("Your start date must be equal or after "+simpleDateFormat.format(orderDate));
        }
    }

    @Override
    public List<Offer> getAgentOffers(String email) {
        return offerDAO.getAgentOffers(email);
    }

    @Override
    public Offer getOfferById(Long id) {
        return offerDAO.getOfferById(id);
    }
}
