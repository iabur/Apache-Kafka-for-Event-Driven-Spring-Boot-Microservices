package com.iabur.emailnotification.handler;

import com.iabur.core.ProductCreatedEvent;
import com.iabur.emailnotification.error.NotRetryableException;
import com.iabur.emailnotification.error.RetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
@KafkaListener(topics = "product-created-event-topic")
public class ProductCreatedEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(ProductCreatedEventHandler.class);
    private final RestTemplate restTemplate;

    public ProductCreatedEventHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @KafkaHandler
    public void handle(@Payload ProductCreatedEvent event,
                       @Header("messageId") String messageId,
                       @Header(KafkaHeaders.RECEIVED_KEY) String messageKey) {
        logger.info("Product created event received: {}", event != null ? event.getTitle() : "<null>");

        try {
            ResponseEntity<String> response =
                    restTemplate.getForEntity("http://localhost:8082/response/200", String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Response from external service: {}", response.getBody());
            }

        } catch (ResourceAccessException e) {
            throw new RetryableException(e);

        } catch (Exception e) {
            throw new NotRetryableException(e);

        } finally {
            logger.info("Product created event handled: {}", event != null ? event.getTitle() : "<null>");
        }
    }
}
