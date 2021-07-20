package com.mycode.telegramagent.services.Impl;

import com.mycode.telegramagent.dao.Interface.OfferDAO;
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
    public Offer saveOffer(String userId, String agencyName, String agencyNumber, MultipartFile file) {
        return offerDAO.saveOffer(userId, agencyName, agencyNumber, file);
    }

    @Override
    public Offer getOfferById(Long id) {
        return offerDAO.getOfferById(id);
    }
}
