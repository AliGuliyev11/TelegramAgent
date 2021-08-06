package com.mycode.telegramagent.rabbitmq.offerOrder.consumer;

import com.mycode.telegramagent.dto.ReplyToOffer;
import com.mycode.telegramagent.services.Interface.IOfferService;
import com.mycode.telegramagent.services.Interface.IOrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote This service class for queue management
 * When publisher send message consumer get message from this class
 */

@Service
public class Consumer {

    private final IOrderService orderService;
    private final IOfferService offerService;

    public Consumer(IOrderService orderService, IOfferService offerService) {
        this.orderService = orderService;
        this.offerService = offerService;
    }

    @RabbitListener(queues = "orderQueue")
    public void onMessageSendOrder(Map<String,String> order) {
        System.out.println(order);
        orderService.addOrder(order);
    }

    @RabbitListener(queues = "stopOrderQueue")
    @Transactional(propagation = Propagation.REQUIRED)
    public void stopMessageSendOrder(String uuid) {
        System.out.println(uuid);
        orderService.requestStatusDeActive(uuid);
    }

    @RabbitListener(queues = "offerReplyQueue")
    @Transactional(propagation = Propagation.REQUIRED)
    public void offerReply(ReplyToOffer replyToOffer) {
        System.out.println(replyToOffer);
        offerService.offerAccepted(replyToOffer);
    }

}
