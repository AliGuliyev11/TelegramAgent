package com.mycode.telegramagent.services.Impl;

import com.mycode.telegramagent.dao.Interface.OfferDAO;
import com.mycode.telegramagent.dto.OfferDto;
import com.mycode.telegramagent.enums.AgentRequestStatus;
import com.mycode.telegramagent.exceptions.*;
import com.mycode.telegramagent.models.Offer;
import com.mycode.telegramagent.models.UserRequest;
import com.mycode.telegramagent.services.Interface.IOfferService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static com.mycode.telegramagent.utils.Validation.validation;

@Service
public class OfferServiceImpl implements IOfferService {



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

    @Override
    public List<Offer> getAgentOffers(String email) {
        return offerDAO.getAgentOffers(email);
    }

    @Override
    public Offer getOfferById(Long id) {
        return offerDAO.getOfferById(id);
    }
}
