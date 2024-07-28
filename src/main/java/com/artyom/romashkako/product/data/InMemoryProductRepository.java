package com.artyom.romashkako.product.data;

import com.artyom.romashkako.product.model.Product;

import java.util.*;


public class InMemoryProductRepository implements ProductRepository {

    private final Map<Integer, Product> productMap = new HashMap<>();

    @Override
    public Product save(Product entity) {
        Objects.requireNonNull(entity);
        var id = Objects.requireNonNullElse(entity.getId(), getNextId());
        entity.setId(id);
        productMap.put(id, entity);
        return entity;
    }

    private int getNextId() {
        return productMap
                .keySet()
                .stream()
                .max(Comparator.naturalOrder())
                .orElse(0) + 1;
    }

    @Override
    public List<Product> findAll() {
        return productMap
                .values()
                .stream()
                .sorted()
                .toList();
    }

    @Override
    public Optional<Product> findById(Integer id) {
        return Optional.ofNullable(productMap.get(id));
    }

    @Override
    public int deleteProductById(Integer id) {
        return productMap.remove(id) != null ? 1 : 0;
    }
}
