package com.iabur.kafkademo.controller;

import com.iabur.kafkademo.model.ErrorMessage;
import com.iabur.kafkademo.model.ProductCreateRequestModel;
import com.iabur.kafkademo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.logging.Logger;

@RestController
@RequestMapping("/product")
public class ProductController {
    public final ProductService productService;
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody ProductCreateRequestModel productCreateRequestMode) {
        String productId = null;
        try {
            productId = productService.createProduct(productCreateRequestMode);
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMessage(new Date(), e.getMessage(), "/product"));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productId);

    }
}
