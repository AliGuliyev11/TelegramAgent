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
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
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
//        if (userRequest == null) {
//            throw new RequestNotFound();
//        } else if (userRequest.getAgentRequestStatus().equals(AgentRequestStatus.Offer_Made) ||
//                userRequest.getAgentRequestStatus().equals(AgentRequestStatus.Accepted)) {
//            throw new YouAlreadyMakeOffer();
//        } else if (userRequest.getAgentRequestStatus().equals(AgentRequestStatus.Expired)) {
//            throw new RequestExpired();
//        } else if (userRequest.getRequestStatus().equals(RequestStatus.De_Active)) {
//            throw new RequestInArchive();
//        }
        JSONObject jsonObject = new JSONObject(userRequest.getUserRequest());
        validation(offerDto, jsonObject.getInt("Orderbudget"));
        checkStartDate(offerDto.getStartDate(), jsonObject.getString("Orderdate"), jsonObject.getLong("Orderdateto"));
        checkEndDate(offerDto.getEndDate(), jsonObject.getString("Orderdate"), jsonObject.getLong("Orderdateto"));

        return offerDAO.saveOffer(userId, email, offerDto);
    }

    @SneakyThrows
    private void checkEndDate(String endDate, String orderDate, long orderDateTo) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date end = simpleDateFormat.parse(endDate);
        Date orderEnd = simpleDateFormat.parse(orderDate);
        long days = ChronoUnit.DAYS.between(LocalDateTime.ofInstant(orderEnd.toInstant(), ZoneId.systemDefault())
                , LocalDateTime.ofInstant(end.toInstant(), ZoneId.systemDefault()));
        System.out.println(days);
        if (days > orderDateTo) {
            throw new CheckStartDate("Your end date must be equal start date or after " + orderDateTo + " days from this "
                    + simpleDateFormat.format(orderEnd));
        }
    }

    @Override
    public void offerAccepted(ReplyToOffer replyToOffer) {
        offerDAO.offerAccepted(replyToOffer);
    }

    @SneakyThrows
    public void checkStartDate(String startDate, String orderDate, Long orderDateTo) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start = simpleDateFormat.parse(startDate);
        Date end = simpleDateFormat.parse(orderDate);
        if (start.before(end) && !orderDate.equals(startDate)) {
            throw new CheckStartDate("Your start date must be equal or after " + simpleDateFormat.format(end));
        }
        long days = ChronoUnit.DAYS.between(LocalDateTime.ofInstant(end.toInstant(), ZoneId.systemDefault())
                , LocalDateTime.ofInstant(start.toInstant(), ZoneId.systemDefault()));
        System.out.println(days);
        if (days > orderDateTo) {
            throw new CheckStartDate("Your start date must be equal " + simpleDateFormat.format(end) + " or after max " + orderDateTo + " days");
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
        Offer offer = offerDAO.getOfferById(email, id);
        if (offer == null) {
            throw new NotAnyOffer();
        }
        return offer;
    }
}
