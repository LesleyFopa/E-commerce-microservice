package org.lesley.ecommerce.kafka;

import org.lesley.ecommerce.entity.PaymentMethod;

import java.math.BigDecimal;

public record PaymentConfirmation(
        String orderReference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String CustomerFirstName,
        String CustomerLastName,
        String CustomerEmail
) {
}
