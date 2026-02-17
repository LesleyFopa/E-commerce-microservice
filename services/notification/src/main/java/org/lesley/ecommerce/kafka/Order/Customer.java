package org.lesley.ecommerce.kafka.Order;

public record Customer(
        String id,
        String firstname,
        String lastname,
        String email
) {
}
