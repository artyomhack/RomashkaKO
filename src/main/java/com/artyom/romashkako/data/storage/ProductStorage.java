package com.artyom.romashkako.data.storage;

import com.artyom.romashkako.model.Product;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Getter
public class ProductStorage {

    private final List<Product> flowers;

    public ProductStorage() {
        this.flowers =  new ArrayList<>(Arrays.asList(
                new Product(1, "Роза", "Красный цветок с шипами", BigDecimal.valueOf(34.99), true),
                new Product(2, "Тюльпан", "Весенний цветок различных оттенков", BigDecimal.valueOf(20.99), true),
                new Product(3, "Гвоздика", "Цветок с гофрированными лепестками", BigDecimal.valueOf(20.99), false),
                new Product(4, "Пионы", "Крупный цветок с пышными лепестками", BigDecimal.valueOf(29.99), true),
                new Product(5, "Лилии", "Элегантный цветок с сильным ароматом", BigDecimal.valueOf(25.99), false),
                new Product(6, "Ромашка", "Маленький белый цветок с желтой серединкой", BigDecimal.valueOf(5.99), true)
        ));
    }
}
