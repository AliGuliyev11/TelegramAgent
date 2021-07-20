package com.mycode.telegramagent.services.Interface;

import com.mycode.telegramagent.models.Offer;
import org.springframework.web.multipart.MultipartFile;

public interface IOfferService {
    Offer saveOffer(String userId, String agencyName, String agencyNumber, MultipartFile file);
    Offer getOfferById(Long id);
}
