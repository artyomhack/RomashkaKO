package com.artyom.romashkako.product.data;

import com.artyom.romashkako.product.model.Product;
import com.artyom.romashkako.product.utils.ProductUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class JdbcProductRepositoryTest {

    @Autowired
    JdbcProductRepository productRepository;

    ProductUtils productUtils = new ProductUtils();

    @Test
    public void shouldAddNewProductInDatabase() {
        var expect = productUtils.createRandomProduct();
        var expectedSize = productRepository.findAll().size() + 1;
        assertNull(expect.getId());

        var actual = productRepository.save(expect);
        var actualSize = productRepository.findAll().size();

        assertNotNull(actual.getId());
        assertEquals(expect.getTitle(), actual.getTitle());
        assertEquals(expect.getDescription(), actual.getDescription());
        assertEquals(expect.getPrice(), actual.getPrice());
        assertEquals(expect.isAvailable(), actual.isAvailable());
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void shouldReturnExistProductByIdFromDataBase() {
        var expect = productRepository.save(productUtils.createRandomProduct());
        var id = expect.getId();

        var actual = productRepository.findById(id);

        assertTrue(actual.isPresent());
        assertEquals(expect, actual.get());
    }

    @Test
    public void shouldReturnNotExistProductByIdFromDataBase() {
        var id = Integer.MAX_VALUE;

        if (productRepository.findById(id).isPresent())
            productRepository.deleteProductById(id);

        var actual = productRepository.findById(id);

        assertTrue(actual.isEmpty());
    }

    @Test
    public void shouldReturnAllProducts() {
        var expectProducts = Stream.of(
                new Product(null, "Роза", "Красный цветок с шипами", 34.99, true),
                new Product(null, "Тюльпан", "Весенний цветок различных оттенков", 20.99, true),
                new Product(null, "Гвоздика", "Цветок с гофрированными лепестками", 20.99, false),
                new Product(null, "Пионы", "Крупный цветок с пышными лепестками", 29.99, true),
                new Product(null, "Лилии", "Элегантный цветок с сильным ароматом", 25.99, false),
                new Product(null, "Ромашка", "Маленький белый цветок с желтой серединкой", 5.99, true)
        ).map(productRepository::save).toList();

        var actualList = productRepository.findAll();

        assertThat(actualList).containsSequence(expectProducts);
    }
}