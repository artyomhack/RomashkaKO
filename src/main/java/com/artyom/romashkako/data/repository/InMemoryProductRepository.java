package com.artyom.romashkako.data.repository;

import com.artyom.romashkako.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;


@RequiredArgsConstructor
@Repository
public class InMemoryProductRepository implements CrudRepository<Product, Integer> {

    private final List<Product> flowers = new ArrayList<>(Arrays.asList(
            new Product(1, "Роза", "Красный цветок с шипами", BigDecimal.valueOf(34.99), true),
            new Product(2, "Тюльпан", "Весенний цветок различных оттенков", BigDecimal.valueOf(20.99), true),
            new Product(3, "Гвоздика", "Цветок с гофрированными лепестками", BigDecimal.valueOf(20.99), false),
            new Product(4, "Пионы", "Крупный цветок с пышными лепестками", BigDecimal.valueOf(29.99), true),
            new Product(5, "Лилии", "Элегантный цветок с сильным ароматом", BigDecimal.valueOf(25.99), false),
            new Product(6, "Ромашка", "Маленький белый цветок с желтой серединкой", BigDecimal.valueOf(5.99), true)
    ));

    @Override
    public <S extends Product> S save(S entity) {
        if (entity != null) {
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
    public void deleteById(Integer id) {
        if (isValidId(id)) {
            flowers.remove(id - 1);
        } else {
            throw new NoSuchElementException("Product by id [" + id + "] not found.");
        }
    }

    private boolean isValidId(Integer id) {
        return id > 0 && id <= flowers.size();
    }
}
