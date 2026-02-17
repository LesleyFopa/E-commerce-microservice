package org.lesley.ecommerce.product;

import java.math.BigDecimal;

public record PurchaseReponse(
        Integer productId,
        String name,
        String description,
        BigDecimal price,
        double quantity
) {
}
