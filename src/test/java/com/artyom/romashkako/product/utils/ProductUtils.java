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
import java.nio.charset.StandardCharsets;
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
        return getRandomProduct(title);
    }

    public Product getRandomProduct(Integer id) {
        return getRandomProduct(id, null,null, null, null);
    }

    public Product getRandomProduct(String title) {
        return getRandomProduct(title, null, null, null);
    }

    public Product getRandomProduct(String title, String description, Double price, Boolean isAvailable) {
        return getRandomProduct(null, title, null, null,false);
    }

    public Product getRandomProduct(Integer id, String title, String description, Double price, Boolean isAvailable) {
        return new Product(
                id, //id can be null when save a new entity
                Objects.requireNonNullElse(title, "title_" + UUID.randomUUID()),
                Objects.requireNonNullElse(description, "description_" + UUID.randomUUID()),
                Objects.requireNonNullElse(price, RANDOM.nextDouble()),
                Objects.requireNonNullElse(isAvailable, RANDOM.nextBoolean())
        );
    }

    public ProductResponse getProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                String.valueOf(product.getPrice()),
                product.isAvailable()
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

    public List<Product> getProductForSorting() {
        return List.of(
                new Product(1, Character.toString(RANDOM.nextInt(64, 90)), "MyDescription number " + RANDOM.nextInt(),
                        RANDOM.nextDouble(0.0, 250.0), RANDOM.nextBoolean()),
                new Product(2, Character.toString(RANDOM.nextInt(64, 90)), "MyDescription number " + RANDOM.nextInt(),
                        RANDOM.nextDouble(0.0, 250.0), RANDOM.nextBoolean()),
                new Product(3, Character.toString(RANDOM.nextInt(64, 90)), "MyDescription number " + RANDOM.nextInt(),
                        RANDOM.nextDouble(0.0, 250.0), RANDOM.nextBoolean()),
                new Product(4, Character.toString(RANDOM.nextInt(64, 90)), "MyDescription number " + RANDOM.nextInt(),
                        RANDOM.nextDouble(0.0, 250.0), RANDOM.nextBoolean()),
                new Product(5, Character.toString(RANDOM.nextInt(64, 90)), "MyDescription number " + RANDOM.nextInt(),
                        RANDOM.nextDouble(0.0, 250.0), RANDOM.nextBoolean()),
                new Product(6, Character.toString(RANDOM.nextInt(64, 90)), "MyDescription number " + RANDOM.nextInt(),
                        RANDOM.nextDouble(0.0, 250.0), RANDOM.nextBoolean())
        );
    }
}


