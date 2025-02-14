package com.danielcontador.onebox_technical_test.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record ProductDto(
        @Schema(example = "Eggs")
        @NotNull(message = "The description cannot be null.")
        @NotEmpty(message = "The description cannot be empty.")
        @Pattern(regexp = "^(?=.*\\S).+$", message = "Description cannot be only spaces.")
        @Size(max = 100, message = "Description cannot exceed 100 characters.")
        String description,
        @Schema(example = "2")
        @Min(value = 1, message = "The minimum amount is 1")
        @Max(value = 1000, message = "The maximum amount is 1000")
        int amount
) {

}
