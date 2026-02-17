package org.lesley.ecommerce.dtos;

import org.lesley.ecommerce.entity.Address;

public record CustomerResponse(
        String id,
        String name,
        String lastname,
        String email,
        Address address
) {
}
