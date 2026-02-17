package org.lesley.ecommerce.payment;

import org.lesley.ecommerce.customer.CustomerResponse;
import org.lesley.ecommerce.entity.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        Integer id,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
