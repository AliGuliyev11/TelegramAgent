package com.mycode.telegramagent.dao.Interface;

import com.mycode.telegramagent.dto.OfferDto;
import com.mycode.telegramagent.models.Offer;
import org.springframework.web.multipart.MultipartFile;

public interface OfferDAO {
    Offer saveOffer(String userId, String email, OfferDto offerDto);
    Offer getOfferById(Long id);
}
