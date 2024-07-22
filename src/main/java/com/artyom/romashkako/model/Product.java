package com.artyom.romashkako.model;

import com.artyom.romashkako.dto.ProductRequest;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class Product {
    private Integer id;
    private String title;
    private String description;
    private BigDecimal price;
    private boolean isAvailable;
}
