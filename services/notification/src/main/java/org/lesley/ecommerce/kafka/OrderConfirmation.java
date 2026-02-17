package org.lesley.ecommerce.kafka;

import org.lesley.ecommerce.kafka.Order.Customer;
import org.lesley.ecommerce.kafka.Order.Product;
import org.lesley.ecommerce.kafka.payment.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
    String orderReference,
    BigDecimal totalAmount,
    PaymentMethod paymentMethod,
    Customer customer,
    List<Product> products
) {
}
