package com.artyom.romashkako.product.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.Comparator;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Product implements Comparable<Product> {
    private Integer id;
    private String title;
    private String description;
    private BigDecimal price;
    private boolean isAvailable;

    @Override
    public int compareTo(Product o) {
        return this.getId().compareTo(o.getId());
    }
}
