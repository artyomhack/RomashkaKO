package com.artyom.romashkako.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRequest {
    @Size(max = 255, message = "Title should not be more than 255")
    @NotBlank(message = "Title has not be empty")
    private String title;
    @Size(max = 4096, message = "Description should not be more than 4096")
    private String description;
    @DecimalMin(value = "0.0", inclusive = false, message = "Prices can`t be less than 0")
    private BigDecimal price = BigDecimal.ZERO;
    private boolean isAvailable = false;
}
