package com.mycode.telegramagent.rabbitmq.offerOrder.publisher;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "offerConfig")
public class RabbitMQOfferConfig {

    @Bean(name = "offerQueue")
    Queue rabbitOfferQueue(){
        Queue orderQueue = new Queue("offerQueue", true);
        return orderQueue;
    }

    @Bean(name = "offerExchange")
    DirectExchange rabbitOfferExchange(){
        return new DirectExchange("offerExchange");
    }

    @Bean(name = "offerBind")
    Binding offerBind(@Qualifier("offerQueue") Queue queue, @Qualifier("offerExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("offerKey");
    }

    @Bean
    MessageConverter offerMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

}
