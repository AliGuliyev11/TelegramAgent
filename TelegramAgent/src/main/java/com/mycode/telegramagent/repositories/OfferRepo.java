package com.mycode.telegramagent.repositories;

import com.mycode.telegramagent.models.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OfferRepo extends JpaRepository<Offer, Long> {
    List<Offer> getOffersByAgent_Email(String email);
    @Query(value = "select o from offer o join Agent a ON o.agent.id=a.id where a.email=:email",nativeQuery = false)
    List<Offer> getOffersByAgentEmail(String email);
}
