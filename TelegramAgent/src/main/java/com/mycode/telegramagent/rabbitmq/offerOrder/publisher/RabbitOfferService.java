package com.mycode.telegramagent.rabbitmq.offerOrder.publisher;

import com.mycode.telegramagent.dto.RabbitOffer;
import com.mycode.telegramagent.models.Offer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitOfferService {
    private final RabbitTemplate template;

    public RabbitOfferService(RabbitTemplate template) {
        this.template = template;
    }

    public void send(RabbitOffer offer){
        template.convertAndSend("offerExchange","offerKey",offer);
    }
}
