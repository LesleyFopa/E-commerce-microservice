package org.lesley.ecommerce.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.lesley.ecommerce.entity.PaymentMethod;
import org.lesley.ecommerce.product.PurchaseRequest;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
        Integer id,
        String reference,
        @Positive(message = "Amount must be positive")
        BigDecimal amount,
        @NotNull(message = "Payment method must not be null")
        PaymentMethod paymentMethod,
        @NotNull(message = "Customer ID must not be null")
        @NotEmpty(message = "Customer ID must not be empty")
        @NotBlank(message = "Customer ID must not be blank")
        String customerId,
        @NotEmpty(message = "Products list must not be empty , you should add at least one product to the order")
        List<PurchaseRequest> products
) {
}
