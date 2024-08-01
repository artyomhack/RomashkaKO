package com.artyom.romashkako.product.service;

import com.artyom.romashkako.product.dto.ProductRequest;
import com.artyom.romashkako.product.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse create(ProductRequest request);
    ProductResponse updateById(ProductRequest request, Integer id);
    List<ProductResponse> fetchAll();
    List<ProductResponse> findByCriteria(String title, double priceGT, double priceGL, boolean available, int limit);
    ProductResponse findById(Integer id);
    void deleteById(Integer id);
}
