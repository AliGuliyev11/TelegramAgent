package com.mycode.telegramagent.rabbitmq.offerOrder.publisher;

import com.mycode.telegramagent.dto.RabbitOffer;
import com.mycode.telegramagent.dto.WarningDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.mycode.telegramagent.configs.production.RabbitMQOfferConfig.*;


/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote This service class send messages to bot
 */


@Service
public class RabbitOfferService {
    private final RabbitTemplate template;

    public RabbitOfferService(RabbitTemplate template) {
        this.template = template;
    }

    public void send(RabbitOffer offer) {
        template.convertAndSend(OFFER_EXCHANGE, OFFER_KEY, offer);
    }

    public void warn(WarningDto warning) {
        template.convertAndSend(OFFER_MADE_EXCHANGE, OFFER_MADE_KEY, warning);
    }
}
