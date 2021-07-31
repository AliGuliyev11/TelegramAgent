package com.mycode.telegramagent.services.Interface;

import com.mycode.telegramagent.dto.OfferDto;
import com.mycode.telegramagent.dto.ReplyToOffer;
import com.mycode.telegramagent.models.Offer;

import java.util.List;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public interface IOfferService {
    Offer saveOffer(String userId, String email, OfferDto offerDto);
    Offer getOfferById(Long id, String email);
    List<Offer> getAgentOffers(String email);
    void offerAccepted(ReplyToOffer replyToOffer);

}
