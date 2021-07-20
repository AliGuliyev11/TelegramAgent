package com.mycode.telegramagent.rabbitmq.offerOrder.consumer;

import com.mycode.telegramagent.dto.ReplyToOffer;
import com.mycode.telegramagent.models.Order;
import com.mycode.telegramagent.services.Interface.IOrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Consumer {

    private final IOrderService orderService;

    public Consumer(IOrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = "orderQueue")
    public void onMessageSendOrder(Order order) {
        System.out.println(order);
        orderService.addOrder(order);
    }

    @RabbitListener(queues = "stopOrderQueue")
    @Transactional(propagation = Propagation.REQUIRED)
    public void stopMessageSendOrder(String uuid) {
        System.out.println(uuid);
        orderService.deleteAllByUserId(uuid);
    }

    @RabbitListener(queues = "offerReplyQueue")
    @Transactional(propagation = Propagation.REQUIRED)
    public void offerReply(ReplyToOffer replyToOffer) {
        System.out.println(replyToOffer);
    }

}
