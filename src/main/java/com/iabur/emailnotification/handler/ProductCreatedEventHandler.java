package com.iabur.emailnotification.handler;


import com.iabur.core.ProductCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "product-created-event-topic")
public class ProductCreatedEventHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @KafkaHandler
    public void handle(ProductCreatedEvent event) {
        logger.info("Product created event received: {}", event);
    }
}
