package com.artyom.romashkako.product.data;

import com.artyom.romashkako.product.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class InMemoryProductRepositoryTest {

    private static final Product PRODUCT = new Product(null, "Роза", "Красный цветок с шипами",
            34.99, true);

    @Autowired
    private ProductRepository inMemoryProductRepository;

    @Test
    public void shouldAddedNewProduct() {
        var expected = PRODUCT;

        var result = inMemoryProductRepository.create(PRODUCT);

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

        var size = inMemoryProductRepository.findAll().size();

        for (int id = 1; id <= size; id++) {
            inMemoryProductRepository.deleteProductById(id);
        }

       new ArrayList<>(expectedList).forEach(it -> {
            it.setId(null);
            inMemoryProductRepository.create(it);
        });

        var actualList = inMemoryProductRepository.findAll();

        assertFalse(actualList.isEmpty());
        assertEquals(expectedList.size(), actualList.size());
        assertThat(actualList).containsExactlyElementsOf(expectedList);
    }

    @Test
    public void shouldCheckExistProductById() {
        var expectedProduct = inMemoryProductRepository.create(PRODUCT);
        var id = expectedProduct.getId();

        Product result = inMemoryProductRepository.findById(id).orElse(null);

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

        expectedList.forEach(inMemoryProductRepository::create);

        List<Product> result = inMemoryProductRepository.findAll();

        assertFalse(result.isEmpty());
        assertThat(result).containsSequence(expectedList);
    }

    @Test
    public void shouldDeleteExistProductById() {
        var expected = inMemoryProductRepository.create(PRODUCT);
        var expectedSize = inMemoryProductRepository.findAll().size();
        var id = expected.getId();

        int result = inMemoryProductRepository.deleteProductById(id);

        var actualSize = inMemoryProductRepository.findAll().size();

        assertTrue(inMemoryProductRepository.findById(id).isEmpty());
        assertEquals(expectedSize - 1, actualSize);
        assertEquals( 1, result);
    }

}