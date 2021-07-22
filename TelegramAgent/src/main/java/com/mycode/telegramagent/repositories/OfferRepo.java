package com.mycode.telegramagent.repositories;

import com.mycode.telegramagent.models.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepo extends JpaRepository<Offer, Long> {
    List<Offer> getOffersByAgent_Email(String email);
}
