package com.mycode.telegramagent.dao.Interface;

import com.mycode.telegramagent.models.Offer;
import org.springframework.web.multipart.MultipartFile;

public interface OfferDAO {
    Offer saveOffer(String userId, String agencyName, String agencyNumber, MultipartFile file);
    Offer getOfferById(Long id);
}
