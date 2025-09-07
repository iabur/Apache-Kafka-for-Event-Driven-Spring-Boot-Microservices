package com.iabur.kafkademo.service;

import com.iabur.kafkademo.request.ProductCreateRequestModel;

public interface ProductService {
    String createProduct(ProductCreateRequestModel productCreateRequestModel);
}
