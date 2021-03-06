package com.mycode.telegramagent.dao.Interface;

import com.mycode.telegramagent.dto.OfferDto;
import com.mycode.telegramagent.dto.ReplyToOffer;
import com.mycode.telegramagent.models.Offer;
import com.mycode.telegramagent.models.UserRequest;

import java.util.List;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

public interface OfferDAO {
    Offer saveOffer(String userId, String email, OfferDto offerDto);

    Offer getOfferById(String email, Long id);

    UserRequest getRequestByUUIDAndEmail(String uuid, String email);

    List<Offer> getAgentOffers(String email);

    void offerAccepted(ReplyToOffer replyToOffer);
}
