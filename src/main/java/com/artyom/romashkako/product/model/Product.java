package com.artyom.romashkako.product.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "products")
public class Product implements Comparable<Product> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_seq_gen")
    @SequenceGenerator(name = "products_seq_gen", sequenceName = "seq_products", allocationSize = 1)
    private Integer id;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    private double price;
    @Column(name = "available")
    private boolean isAvailable;

    @Override
    public int compareTo(Product o) {
        return this.getId().compareTo(o.getId());
    }
}
