package org.lesley.ecommerce.kafka;

import org.lesley.ecommerce.customer.CustomerResponse;
import org.lesley.ecommerce.entity.PaymentMethod;
import org.lesley.ecommerce.product.PurchaseReponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseReponse> products
) {
}
