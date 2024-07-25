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
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class InMemoryProductRepositoryTest {

    private static final Product PRODUCT = new Product(null, "Роза", "Красный цветок с шипами",
            34.99, true);

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void shouldAddedNewProduct() {
        var expected = PRODUCT;

        var result = productRepository.save(PRODUCT);

        assertNotNull(result.getId());
        assertEquals(expected.getTitle(), result.getTitle());
        assertEquals(expected.getDescription(), result.getDescription());
        assertEquals(expected.getPrice(), result.getPrice());
        assertEquals(expected.isAvailable(), result.isAvailable());
    }

    @Test
    public void shouldCheckReturnNewIdWhenAddedNewProduct_idHasNotBeMatch() {
        List<Product> expectedList = List.of(
                new Product(1, "Роза", "Красный цветок с шипами", 34.99, true),
                new Product(2, "Тюльпан", "Весенний цветок различных оттенков", 20.99, true),
                new Product(3, "Гвоздика", "Цветок с гофрированными лепестками", 20.99, false),
                new Product(4, "Пионы", "Крупный цветок с пышными лепестками", 29.99, true),
                new Product(5, "Лилии", "Элегантный цветок с сильным ароматом", 25.99, false),
                new Product(6, "Ромашка", "Маленький белый цветок с желтой серединкой", 5.99, true)
        );

        var size = productRepository.findAll().size();
        for (int id = 1; id <= size; id++) {
            productRepository.deleteById(id);
        }

       new ArrayList<>(expectedList).forEach(it -> {
            it.setId(null);
            productRepository.save(it);
        });

        var actualList = productRepository.findAll();

        assertFalse(actualList.isEmpty());
        assertEquals(expectedList.size(), actualList.size());
        assertThat(actualList).containsExactlyElementsOf(expectedList);
    }

    @Test
    public void shouldCheckExistProductById() {
        var expectedProduct = productRepository.save(PRODUCT);
        var id = expectedProduct.getId();

        Product result = productRepository.findById(id).orElse(null);

        assertNotNull(result);
        assertEquals(result.getId(), id);
        assertEquals(expectedProduct.getTitle(), result.getTitle());
        assertEquals(expectedProduct.getDescription(), result.getDescription());
        assertEquals(expectedProduct.getPrice(), result.getPrice());
        assertEquals(expectedProduct.isAvailable(), result.isAvailable());
    }

    @Test
    public void shouldReturnAllProducts() {
        List<Product> expectedList = List.of(
                new Product(1, "Роза", "Красный цветок с шипами", 34.99, true),
                new Product(2, "Тюльпан", "Весенний цветок различных оттенков", 20.99, true),
                new Product(3, "Гвоздика", "Цветок с гофрированными лепестками", 20.99, false),
                new Product(4, "Пионы", "Крупный цветок с пышными лепестками", 29.99, true),
                new Product(5, "Лилии", "Элегантный цветок с сильным ароматом", 25.99, false),
                new Product(6, "Ромашка", "Маленький белый цветок с желтой серединкой", 5.99, true)
        );

        expectedList.forEach(productRepository::save);

        List<Product> result = productRepository.findAll();

        assertFalse(result.isEmpty());
        assertThat(result).containsSequence(expectedList);
    }

    @Test
    public void shouldDeleteExistProductById() {
        var expected = productRepository.save(PRODUCT);
        var expectedSize = productRepository.findAll().size();
        var id = expected.getId();

        productRepository.deleteById(id);

        var actualSize = productRepository.findAll().size();

        assertTrue(productRepository.findById(id).isEmpty());
        assertEquals(expectedSize - 1, actualSize);
    }

}