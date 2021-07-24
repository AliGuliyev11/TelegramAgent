package com.mycode.telegramagent.services.Interface;

import com.mycode.telegramagent.dto.OfferDto;
import com.mycode.telegramagent.dto.ReplyToOffer;
import com.mycode.telegramagent.models.Offer;
import com.mycode.telegramagent.models.UserRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IOfferService {
    Offer saveOffer(String userId, String email, OfferDto offerDto);
    Offer getOfferById(Long id);
    List<Offer> getAgentOffers(String email);
    void offerAccepted(ReplyToOffer replyToOffer);

}
