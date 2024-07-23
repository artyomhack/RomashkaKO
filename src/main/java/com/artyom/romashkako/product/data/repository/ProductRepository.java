package com.artyom.romashkako.product.data.repository;

import com.artyom.romashkako.product.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product entity);
    List<Product> findAll();
    Optional<Product> findById(Integer id);
    void deleteById(Integer id);
}
