package com.artyom.romashkako.product.data;

import com.artyom.romashkako.product.utils.ProductUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class InDataBaseProductRepositoryTest {

    @Autowired
    InDataBaseProductRepository productRepository;

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
            productRepository.deleteById(id);

        var actual = productRepository.findById(id);

        assertTrue(actual.isEmpty());
    }
}