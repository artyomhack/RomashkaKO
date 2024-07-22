package com.artyom.romashkako.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRequest {
    @Size(max = 255, message = "Title should not be more than 255")
    @NotEmpty(message = "Title has not be empty")
    @NotBlank(message = "Title has not be blank")
    private String title;
    @Size(max = 4096, message = "Description should not be more than 4096")
    private String description;
    @DecimalMin(value = "0.0", inclusive = false)
    @Builder.Default
    private BigDecimal price = BigDecimal.ZERO;
    @Builder.Default
    private boolean isAvailable = false;
}
