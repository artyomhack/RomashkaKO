package com.artyom.romashkako.product.utils;

import com.artyom.romashkako.product.data.ProductRepository;
import com.artyom.romashkako.product.dto.ProductRequest;
import com.artyom.romashkako.product.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import java.math.BigDecimal;
import java.util.*;

@TestComponent
public class ProductUtils {

    private final static Random RANDOM = new Random();

    @Autowired
    private ProductRepository productRepository;

    public Product createRandomProduct() {
        return createRandomProduct(null);
    }

    public Product createRandomProduct(String title) {
        return productRepository.save(getRandomProduct(null));
    }

    public Product getRandomProduct() {
        return getRandomProduct(null);
    }

    public Product getRandomProduct(String title) {
        return getRandomProduct(title, null, null, null);
    }

    public Product getRandomProduct(String title, String description, BigDecimal price, Boolean isAvailable) {
        return new Product(
                null,
                Objects.requireNonNullElse(title, "name_" + UUID.randomUUID()),
                Objects.requireNonNullElse(description, "description_" + UUID.randomUUID()),
                Objects.requireNonNullElse(price, BigDecimal.valueOf(RANDOM.nextDouble())),
                Objects.requireNonNullElse(isAvailable, RANDOM.nextBoolean())
        );
    }

    public ProductRequest getProductRequest(Product product) {
        return new ProductRequest(
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.isAvailable()
        );
    }
}


