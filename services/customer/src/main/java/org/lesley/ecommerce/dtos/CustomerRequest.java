package org.lesley.ecommerce.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.lesley.ecommerce.entity.Address;

public record CustomerRequest(

        String id,

        @NotNull(message = "Customer firstname is required")
        String firstname,

        @NotNull(message = "Customer lastname is required")
        String lastname,

        @NotNull(message = "Customer email is required")
        @Email(message = "Customer email is mot valid")
        String email,

        Address address
) {
}
