package com.artyom.romashkako.data.repository;

import com.artyom.romashkako.model.Product;
import com.artyom.romashkako.data.storage.ProductStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@RequiredArgsConstructor
@Repository
public class InMemoryProductRepository implements CrudRepository<Product, Integer> {

    private final ProductStorage productStorage;

    @Override
    public <S extends Product> S save(S entity) {
        if (entity != null) {
            List<Product> flowers = productStorage.getFlowers();
            if (entity.getId() == null || entity.getId() == 0) {
                entity.setId(flowers.size() + 1);
                flowers.add(entity);
            } else {
                flowers.set(entity.getId() - 1, entity);
            }
            return entity;
        } else {
            throw new IllegalArgumentException("Entity is null.");
        }
    }

    @Override
    public Iterable<Product> findAll() {
        return productStorage.getFlowers().isEmpty() ?
                Collections.emptyList() :
                productStorage.getFlowers();
    }

    @Override
    public Optional<Product> findById(Integer id) {
        if (isValidId(id)) {
            List<Product> flowers = productStorage.getFlowers();
            return Optional.of(flowers.get(id - 1));
        } else {
            throw new NoSuchElementException("Product by id [" + id + "] not found.");
        }
    }

    @Override
    public void deleteById(Integer id) {
        if (isValidId(id)) {
            List<Product> flowers = productStorage.getFlowers();
            flowers.remove(id - 1);
        } else {
            throw new NoSuchElementException("Product by id [" + id + "] not found.");
        }
    }

    private boolean isValidId(Integer id) {
        return id > 0 && id <= productStorage.getFlowers().size();
    }
}
