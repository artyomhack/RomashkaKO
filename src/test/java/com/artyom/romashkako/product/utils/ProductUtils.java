package com.artyom.romashkako.product.utils;

import com.artyom.romashkako.product.data.ProductRepository;
import com.artyom.romashkako.product.dto.ProductRequest;
import com.artyom.romashkako.product.dto.ProductResponse;
import com.artyom.romashkako.product.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

@TestComponent
public class ProductUtils {

    final static Random RANDOM = new Random();

    @Autowired
    private ProductRepository productRepository;

    public Product createRandomProduct() {
        return createRandomProduct(null);
    }

    public Product createRandomProduct(String title) {
        return productRepository.save(getRandomProduct(title));
    }

    public Product getRandomProduct() {
        return getRandomProduct(null);
    }

    public Product getRandomProduct(String title) {
        return getRandomProduct(title, null, null, null);
    }

    public Product getRandomProduct(String title, String description, Double price, Boolean isAvailable) {
        return new Product(
                null,
                Objects.requireNonNullElse(title, "title_" + UUID.randomUUID()),
                Objects.requireNonNullElse(description, "description_" + UUID.randomUUID()),
                Objects.requireNonNullElse(price, RANDOM.nextDouble()),
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


    public <T> T readJson(ResultActions actions, Class<T> t) {
        try {
            var json = actions.andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);

            return new ObjectMapper().readValue(json, t);
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}


