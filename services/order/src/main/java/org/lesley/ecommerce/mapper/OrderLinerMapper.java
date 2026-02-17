package org.lesley.ecommerce.mapper;

import org.lesley.ecommerce.dtos.OrderLineRequest;
import org.lesley.ecommerce.dtos.OrderLineResponse;
import org.lesley.ecommerce.entity.Order;
import org.lesley.ecommerce.entity.OrderLine;
import org.springframework.stereotype.Service;

@Service
public class OrderLinerMapper {
    public OrderLine toOrderLine(OrderLineRequest request) {
        return OrderLine.builder()
                .id(request.Id())
                .quantity(request.quantity())
                .order(
                        Order.builder()
                                .id(request.orderId())
                                .build()
                )
                .productId(request.productId())
                .build();
    }

    public OrderLineResponse toOrderLineResponse(OrderLine orderLine) {
        return new OrderLineResponse(
                orderLine.getId(),
                orderLine.getQuantity()
        );
    }
}
