package org.lesley.ecommerce.dtos;

import lombok.Builder;

@Builder
public record CategoryResponse(
        Integer id,
        String name,
        String description
) {}