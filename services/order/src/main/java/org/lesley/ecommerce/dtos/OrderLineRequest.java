package org.lesley.ecommerce.dtos;

public record OrderLineRequest(
        Integer Id,
        Integer orderId,
        Integer productId,
        double quantity
) {
}
