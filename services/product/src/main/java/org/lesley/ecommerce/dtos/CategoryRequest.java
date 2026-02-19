package org.lesley.ecommerce.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CategoryRequest(
        @NotBlank(message = "Category name is required")
        String name,
        String description
) {}