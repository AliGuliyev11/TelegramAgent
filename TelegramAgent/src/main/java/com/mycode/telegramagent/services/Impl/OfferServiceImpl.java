package com.mycode.telegramagent.services.Impl;

import com.mycode.telegramagent.dao.Interface.OfferDAO;
import com.mycode.telegramagent.dto.OfferDto;
import com.mycode.telegramagent.enums.AgentRequestStatus;
import com.mycode.telegramagent.exceptions.*;
import com.mycode.telegramagent.models.Offer;
import com.mycode.telegramagent.models.UserRequest;
import com.mycode.telegramagent.services.Interface.IOfferService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

@Service
public class OfferServiceImpl implements IOfferService {


    private final String DATE_REGEX="\\d{2}[.]\\d{2}[.]\\d{4}";

    private final OfferDAO offerDAO;

    public OfferServiceImpl(OfferDAO offerDAO) {
        this.offerDAO = offerDAO;
    }

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
        validation(offerDto);


        return offerDAO.saveOffer(userId, email, offerDto);
    }

    private void validation(OfferDto offerDto) {

        if (offerDto.getDescription() == null || offerDto.getStartDate() == null || offerDto.getEndDate() == null
                || offerDto.getPrice() == null) {
            throw new OfferValidation();
        }
        if (offerDto.getPrice()<=0){
            throw new OfferPriceZero();
        }
    }

    @Override
    public Offer getOfferById(Long id) {
        return offerDAO.getOfferById(id);
    }
}
