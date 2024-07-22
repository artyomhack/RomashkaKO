package com.artyom.romashkako.service;

import com.artyom.romashkako.dto.ProductRequest;
import com.artyom.romashkako.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse create(ProductRequest request);
    ProductResponse updateById(ProductRequest request, Integer id);
    List<ProductResponse> fetchAll();
    ProductResponse findById(Integer id);
    void deleteById(Integer id);
}
