package com.mycode.telegramagent.rabbitmq.offerOrder.consumer;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote This configuration class for convert publisher messages
 */

@Configuration
public class RabbitMQConfig {

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
