package com.iabur.kafkademo.service.impl;

import com.iabur.kafkademo.request.ProductCreateRequestModel;
import com.iabur.kafkademo.service.ProductCreatedEvent;
import com.iabur.kafkademo.service.ProductService;


import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Service
public class ProductServiceImpl implements ProductService {
    private final String TOPIC = "product-created-event-topic";
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public ProductServiceImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(ProductCreateRequestModel productCreateRequestModel) {
        String productId = UUID.randomUUID().toString();
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId, productCreateRequestModel.getTitle(),
                productCreateRequestModel.getPrice(), productCreateRequestModel.getDescription());

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(TOPIC, productId, productCreatedEvent);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                logger.severe("Error while sending message to kafka topic");
            } else {
                logger.info("Message sent to kafka topic " + TOPIC + " with offset " + result.getRecordMetadata().offset());
            }
        });

        return productId;
    }

}
