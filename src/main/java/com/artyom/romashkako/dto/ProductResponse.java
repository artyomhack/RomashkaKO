package com.artyom.romashkako.dto;

public record ProductResponse(
        Integer id,
        String title,
        String description,
        String price,
        boolean isAvailable
) {

}
