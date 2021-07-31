package com.mycode.telegramagent.services.LifeCycle;

import com.mycode.telegramagent.dto.WarningDto;
import com.mycode.telegramagent.enums.AgentRequestStatus;
import com.mycode.telegramagent.enums.RequestStatus;
import com.mycode.telegramagent.models.UserRequest;
import com.mycode.telegramagent.rabbitmq.offerOrder.publisher.RabbitOfferService;
import lombok.*;

import javax.persistence.PostLoad;
import javax.persistence.PostUpdate;
import java.time.LocalDateTime;

/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote This class for Order entity's EntityListener
 */

@Data
@NoArgsConstructor
public class OrderLifeCycle {

    RabbitOfferService rabbitOfferService;

    public OrderLifeCycle(RabbitOfferService rabbitOfferService) {
        this.rabbitOfferService = rabbitOfferService;
    }

    /**
     * When user request call from db and not empty user request enters this method
     */

    @PostLoad
    private void beforeLoad(UserRequest userRequest) {
        if ((userRequest.getAgentRequestStatus().equals(AgentRequestStatus.Offer_Made) || userRequest.getAgentRequestStatus().equals(AgentRequestStatus.Accepted))
                && userRequest.getExpiredDate().isBefore(LocalDateTime.now())) {
            System.out.println("BB");
            rabbitOfferService.warn(WarningDto.builder().text("Diqqet diqqetttttttttttt").userId(userRequest.getUserId()).build());
        }
        System.out.println("AA");
    }

}
