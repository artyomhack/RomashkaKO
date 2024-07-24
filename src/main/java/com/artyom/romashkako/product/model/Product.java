package com.artyom.romashkako.product.model;

import lombok.*;

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
    private double price;
    private boolean isAvailable;

    @Override
    public int compareTo(Product o) {
        return this.getId().compareTo(o.getId());
    }
}
