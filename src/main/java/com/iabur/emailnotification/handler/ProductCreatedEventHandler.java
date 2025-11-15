package com.iabur.emailnotification.handler;

import com.iabur.core.ProductCreatedEvent;
import com.iabur.emailnotification.error.NotRetryableException;
import com.iabur.emailnotification.error.RetryableException;
import com.iabur.emailnotification.model.ProcessedMessage;
import com.iabur.emailnotification.repository.ProcessedMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Component
@KafkaListener(topics = "product-created-event-topic")
public class ProductCreatedEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(ProductCreatedEventHandler.class);
    private final RestTemplate restTemplate;
    private final ProcessedMessageRepository processedMessageRepository;

    public ProductCreatedEventHandler(RestTemplate restTemplate,
                                      ProcessedMessageRepository processedMessageRepository) {
        this.restTemplate = restTemplate;
        this.processedMessageRepository = processedMessageRepository;
    }

    @KafkaHandler
    public void handle(@Payload ProductCreatedEvent event,
                       @Header("messageId") String messageId,
                       @Header(KafkaHeaders.RECEIVED_KEY) String messageKey) {
        logger.info("Product created event received: {} (messageId={})",
                event != null ? event.getTitle() : "<null>", messageId);

        // Idempotency check: if messageId already processed, skip handling
        if (messageId != null && processedMessageRepository.existsById(messageId)) {
            logger.info("Skipping already-processed messageId={}", messageId);
            return;
        }

        try {
            ResponseEntity<String> response =
                    restTemplate.getForEntity("http://localhost:8082/response/200", String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Response from external service: {}", response.getBody());
            }

            // Mark message as processed (best-effort). If another instance processed it concurrently,
            // a DataIntegrityViolationException may be thrown and we can safely ignore it.
            if (messageId != null) {
                try {
                    ProcessedMessage pm = new ProcessedMessage(messageId, LocalDateTime.now());
                    processedMessageRepository.save(pm);
                } catch (DataIntegrityViolationException ex) {
                    logger.warn("Message already marked processed concurrently (messageId={})", messageId);
                }
            }

        } catch (ResourceAccessException e) {
            throw new RetryableException(e);

        } catch (Exception e) {
            throw new NotRetryableException(e);

        } finally {
            logger.info("Product created event handled: {} (messageId={})",
                    event != null ? event.getTitle() : "<null>", messageId);
        }
    }
}
