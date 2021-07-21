package com.mycode.telegramagent.services.Impl;

import com.mycode.telegramagent.dao.Interface.OfferDAO;
import com.mycode.telegramagent.dto.OfferDto;
import com.mycode.telegramagent.models.Offer;
import com.mycode.telegramagent.services.Interface.IOfferService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OfferServiceImpl implements IOfferService {

    private final OfferDAO offerDAO;

    public OfferServiceImpl(OfferDAO offerDAO) {
        this.offerDAO = offerDAO;
    }

    @Override
    public Offer saveOffer(String userId, String email, OfferDto offerDto) {
        return offerDAO.saveOffer(userId, email, offerDto);
    }

    @Override
    public Offer getOfferById(Long id) {
        return offerDAO.getOfferById(id);
    }
}
