package com.artyom.romashkako.product.data;

import com.artyom.romashkako.product.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class InMemoryProductRepositoryTest {

    private static final Product PRODUCT = new Product(null, "Роза", "Красный цветок с шипами",
            34.99, true);

    private static final List<Product> PRODUCT_LIST = new ArrayList<>(List.of(
            new Product(1, "Роза", "Красный цветок с шипами", 34.99, true),
            new Product(2, "Тюльпан", "Весенний цветок различных оттенков", 20.99, true),
            new Product(3, "Гвоздика", "Цветок с гофрированными лепестками", 20.99, false),
            new Product(4, "Пионы", "Крупный цветок с пышными лепестками", 29.99, true),
            new Product(5, "Лилии", "Элегантный цветок с сильным ароматом", 25.99, false),
            new Product(6, "Ромашка", "Маленький белый цветок с желтой серединкой", 5.99, true)
    ));

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void shouldAddedNewValidatingProduct_success() {
        Product result = productRepository.save(PRODUCT);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertTrue(result.getId() > 0);
        assertEquals(result, PRODUCT);
    }

    @Test
    public void shouldCheckExistProductById_success() {
        productRepository.save(PRODUCT);

        Product result = productRepository.findById(PRODUCT.getId()).orElse(null);

        assertNotNull(result);
        assertEquals(result.getId(), PRODUCT.getId());
        assertEquals(result, PRODUCT);
    }

    @Test
    public void shouldReturnAllProducts_success() {
        PRODUCT_LIST.forEach(productRepository::save);

        List<Product> result = new ArrayList<>(productRepository.findAll());
        result.retainAll(PRODUCT_LIST);

        assertFalse(result.isEmpty());
        assertEquals(PRODUCT_LIST.size(), result.size());
        assertEquals(PRODUCT_LIST, result);
    }

    @Test
    public void shouldDeleteExistProductById_success() {
        productRepository.save(PRODUCT);
        var id = PRODUCT.getId();
        var size = productRepository.findAll().size();
        boolean isDeleted = productRepository.deleteById(id);

        assertTrue(isDeleted);
        assertTrue(productRepository.findById(id).isEmpty());
        assertEquals(size - 1, productRepository.findAll().size());
    }

}