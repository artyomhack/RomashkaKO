package com.artyom.romashkako.product.model;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Product {
    private Integer id;
    private String title;
    private String description;
    private BigDecimal price;
    private boolean isAvailable;
}
