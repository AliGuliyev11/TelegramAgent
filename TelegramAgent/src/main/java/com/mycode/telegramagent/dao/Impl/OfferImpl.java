package com.mycode.telegramagent.dao.Impl;

import com.mycode.telegramagent.dao.Interface.OfferDAO;
import com.mycode.telegramagent.models.Offer;
import com.mycode.telegramagent.rabbitmq.offerOrder.publisher.RabbitOfferService;
import com.mycode.telegramagent.repositories.OfferRepo;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

@Component
public class OfferImpl implements OfferDAO {

    private final OfferRepo offerRepo;
    private final RabbitOfferService offerService;

    public OfferImpl(OfferRepo offerRepo, RabbitOfferService offerService) {
        this.offerRepo = offerRepo;
        this.offerService = offerService;
    }

    @SneakyThrows
    @Override
    public Offer saveOffer(String userId, String agencyName, String agencyNumber, MultipartFile file) {
        File myFile = new File("src/main/resources/static/docs/image.png");
        myFile.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(myFile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();
        File photo = new File(myFile.getAbsolutePath());
        Offer offer = Offer.builder().userId(userId).agencyName(agencyName).agencyNumber(agencyNumber).file(photo).build();
        Offer savedOffer = offerRepo.save(offer);
        offerService.send(savedOffer);
        return savedOffer;
    }

    @Override
    public Offer getOfferById(Long id) {
        return offerRepo.findById(id).get();
    }
}
