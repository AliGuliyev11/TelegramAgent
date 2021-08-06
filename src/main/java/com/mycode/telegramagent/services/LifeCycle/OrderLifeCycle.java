package com.mycode.telegramagent.services.LifeCycle;

import com.mycode.telegramagent.dto.WarningDto;
import com.mycode.telegramagent.enums.AgentRequestStatus;
import com.mycode.telegramagent.models.UserRequest;
import com.mycode.telegramagent.rabbitmq.offerOrder.publisher.RabbitOfferService;
import com.mycode.telegramagent.repositories.JasperMessageRepo;
import com.mycode.telegramagent.services.Locale.LocaleMessageService;
import lombok.*;
import org.aspectj.weaver.ast.Or;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PostLoad;
import java.time.LocalDateTime;

import static com.mycode.telegramagent.utils.GetMessages.getJasperMessage;

/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote This class for Order entity's EntityListener
 */

@Data
@NoArgsConstructor
@Service
public class OrderLifeCycle {

    static private RabbitOfferService rabbitOfferService;
    static private JasperMessageRepo jasperMessageRepo;
    static private LocaleMessageService messageService;


    @Autowired
    public void init(RabbitOfferService rabbitOfferService,JasperMessageRepo jasperMessageRepo, LocaleMessageService messageService) {
        OrderLifeCycle.rabbitOfferService=rabbitOfferService;
        OrderLifeCycle.jasperMessageRepo = jasperMessageRepo;
        OrderLifeCycle.messageService = messageService;
    }

    /**
     * When user request call from db and not empty user request enters this method
     */

    @PostLoad
    private void beforeLoad(UserRequest userRequest) {
        if ((userRequest.getAgentRequestStatus().equals(AgentRequestStatus.Offer_Made) || userRequest.getAgentRequestStatus().equals(AgentRequestStatus.Accepted))
                && userRequest.getExpiredDate().isBefore(LocalDateTime.now())) {
            JSONObject jsonObject = new JSONObject(userRequest.getUserRequest());
            String language = jsonObject.getString("lang");
            rabbitOfferService.warn(WarningDto.builder()
                    .text(getJasperMessage("warning.message", language, jasperMessageRepo, messageService))
                    .userId(userRequest.getUserId()).build());
        }
        System.out.println("AA");
    }

}
