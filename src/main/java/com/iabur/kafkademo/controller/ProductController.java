package com.iabur.kafkademo.controller;

import com.iabur.kafkademo.request.ProductCreateRequestModel;
import com.iabur.kafkademo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {
    public final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity createProduct(@RequestBody ProductCreateRequestModel productCreateRequestMode) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(productCreateRequestMode));

    }
}
