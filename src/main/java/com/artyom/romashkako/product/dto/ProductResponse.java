package com.artyom.romashkako.product.dto;

public record ProductResponse(
        Integer id,
        String title,
        String description,
        String price,
        boolean isAvailable
) {

}
