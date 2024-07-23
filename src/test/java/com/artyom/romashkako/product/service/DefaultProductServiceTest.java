package com.artyom.romashkako.product.service;

import com.artyom.romashkako.common.exception.NotFoundException;
import com.artyom.romashkako.product.dto.ProductRequest;
import com.artyom.romashkako.product.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class DefaultProductServiceTest {

    private static final List<Product> PRODUCT_LIST = new ArrayList<>(List.of(
            new Product(1, "Роза", "Красный цветок с шипами", BigDecimal.valueOf(34.99), true),
            new Product(2, "Тюльпан", "Весенний цветок различных оттенков", BigDecimal.valueOf(20.99), true),
            new Product(3, "Гвоздика", "Цветок с гофрированными лепестками", BigDecimal.valueOf(20.99), false),
            new Product(4, "Пионы", "Крупный цветок с пышными лепестками", BigDecimal.valueOf(29.99), true),
            new Product(5, "Лилии", "Элегантный цветок с сильным ароматом", BigDecimal.valueOf(25.99), false),
            new Product(6, "Ромашка", "Маленький белый цветок с желтой серединкой", BigDecimal.valueOf(5.99), true)
    ));

    @Mock
    DefaultProductService productService;

    @Test
    public void shouldCatchNotFound_whenUpdatedUserById_success() {
        ProductRequest product = new ProductRequest("Шиповник", "Кислая ягода", BigDecimal.valueOf(29.30), true);
        doThrow(NotFoundException.class).when(productService).updateById(product, Integer.MAX_VALUE);
        assertThrows(NotFoundException.class, () -> productService.updateById(product, Integer.MAX_VALUE));
    }

    @Test
    public void shouldCatchNotFound_whenFetchProductById_success() {
        var id = Integer.MAX_VALUE;
        doThrow(NotFoundException.class).when(productService).findById(id);
        assertThrows(NotFoundException.class, () -> productService.findById(id));
    }

    @Test
    public void shouldCatchNotFound_whenDeleteProductById_success() {
        var id = Integer.MAX_VALUE;
        doThrow(NotFoundException.class).when(productService).deleteById(Integer.MAX_VALUE);
        assertThrows(NotFoundException.class, () -> productService.deleteById(id));
    }
}