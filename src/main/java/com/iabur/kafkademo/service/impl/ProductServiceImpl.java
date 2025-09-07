package com.iabur.kafkademo.service.impl;

import com.iabur.kafkademo.model.ProductCreateRequestModel;
import com.iabur.kafkademo.service.ProductCreatedEvent;
import com.iabur.kafkademo.service.ProductService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
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
    public String createProduct(ProductCreateRequestModel productCreateRequestModel) throws ExecutionException, InterruptedException {
        String productId = UUID.randomUUID().toString();
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId, productCreateRequestModel.getTitle(), productCreateRequestModel.getPrice(), productCreateRequestModel.getDescription());

        logger.info("Sending product created event to kafka topic");
        SendResult<String, Object> future = kafkaTemplate.send(TOPIC, productId, productCreatedEvent).get();
        //partition and offset, topic
        logger.info("Message sent to partition " + future.getRecordMetadata().partition() + " with offset " + future.getRecordMetadata().offset() + " to topic " + future.getRecordMetadata().topic());

        if (future.getRecordMetadata() != null) {
            logger.info("Product created successfully");
        }

        return productId;
    }

}
