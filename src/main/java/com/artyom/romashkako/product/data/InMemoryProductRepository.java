package com.artyom.romashkako.product.data;

import com.artyom.romashkako.product.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class InMemoryProductRepository implements ProductRepository {

    private final List<Product> flowers;

    @Override
    public Product save(Product entity) {
        if (entity != null) {
            if (entity.getId() == null || entity.getId() == 0) {
                entity.setId(flowers.size() + 1);
                flowers.add(entity);
            } else {
                flowers.set(entity.getId() - 1, entity);
            }
            return entity;
        } else {
            throw new NullPointerException("Entity is null.");
        }
    }

    @Override
    public List<Product> findAll() {
        return flowers.isEmpty() ? Collections.emptyList() : flowers;
    }

    @Override
    public Optional<Product> findById(Integer id) {
        if (isValidId(id)) {
            return Optional.of(flowers.get(id - 1));
        } else {
            throw new NoSuchElementException("Product by id [" + id + "] not found.");
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        if (isValidId(id)) {
            flowers.remove(id - 1);
            return true;
        } else {
            throw new NoSuchElementException("Product by id [" + id + "] not found.");
        }
    }

    private boolean isValidId(Integer id) {
        return id > 0 && id <= flowers.size();
    }
}
