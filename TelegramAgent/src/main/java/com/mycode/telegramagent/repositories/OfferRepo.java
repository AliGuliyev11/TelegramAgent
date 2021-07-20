package com.mycode.telegramagent.repositories;

import com.mycode.telegramagent.models.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepo extends JpaRepository<Offer,Long> {
}
