package com.iabur.kafkademo.service;

import com.iabur.kafkademo.request.ProductCreateRequestModel;

import java.util.concurrent.ExecutionException;

public interface ProductService {
    String createProduct(ProductCreateRequestModel productCreateRequestModel) throws ExecutionException, InterruptedException;
}
