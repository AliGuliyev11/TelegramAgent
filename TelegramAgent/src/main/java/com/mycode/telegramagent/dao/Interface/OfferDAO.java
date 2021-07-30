package com.mycode.telegramagent.dao.Interface;

import com.mycode.telegramagent.dto.OfferDto;
import com.mycode.telegramagent.dto.ReplyToOffer;
import com.mycode.telegramagent.models.Offer;
import com.mycode.telegramagent.models.UserRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface OfferDAO {
    Offer saveOffer(String userId, String email, OfferDto offerDto);
    Offer getOfferById(String email,Long id);
    UserRequest getRequestByUUIDAndEmail(String uuid, String email);
    List<Offer> getAgentOffers(String email);
    void offerAccepted(ReplyToOffer replyToOffer);
}
