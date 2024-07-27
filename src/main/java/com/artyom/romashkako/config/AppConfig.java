package com.artyom.romashkako.config;

import com.artyom.romashkako.common.exception.ValidationException;
import com.artyom.romashkako.common.mapper.ValidationExceptionMapper;
import com.artyom.romashkako.product.data.InMemoryProductRepository;
import com.artyom.romashkako.product.data.ProductRepository;
import com.artyom.romashkako.product.mapper.ProductMapper;
import com.artyom.romashkako.product.model.Product;
import com.artyom.romashkako.product.service.ProductService;
import com.artyom.romashkako.product.service.DefaultProductService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class AppConfig {

    @Bean
    @ConditionalOnMissingBean(ProductRepository.class)
    public ProductRepository productRepository() {
        var repository = new InMemoryProductRepository();

        List.of(
                new Product(1, "Роза", "Красный цветок с шипами", 34.99, true),
                new Product(2, "Тюльпан", "Весенний цветок различных оттенков",20.99, true),
                new Product(3, "Гвоздика", "Цветок с гофрированными лепестками", 20.99, false),
                new Product(4, "Пионы", "Крупный цветок с пышными лепестками", 29.99, true),
                new Product(5, "Лилии", "Элегантный цветок с сильным ароматом", 25.99, false),
                new Product(6, "Ромашка", "Маленький белый цветок с желтой серединкой", 5.99, true)
        ).forEach(repository::save);

        return repository;
    }

    @Bean
    @ConditionalOnMissingBean(ProductService.class)
    public ProductService productService(ProductRepository repository, ProductMapper mapper) {
        return new DefaultProductService(repository, mapper);
    }

}
