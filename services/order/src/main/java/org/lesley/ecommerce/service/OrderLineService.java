package org.lesley.ecommerce.service;

import org.lesley.ecommerce.dtos.OrderLineRequest;
import org.lesley.ecommerce.dtos.OrderLineResponse;
import org.lesley.ecommerce.mapper.OrderLinerMapper;
import org.lesley.ecommerce.repository.OrderLineRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderLineService {

    private final OrderLineRepository orderLineRepository;
    private final OrderLinerMapper mapper;

    public OrderLineService(OrderLineRepository orderLineRepository, OrderLinerMapper mapper) {
        this.orderLineRepository = orderLineRepository;
        this.mapper = mapper;
    }

    public Integer saveOrderLine(OrderLineRequest orderLineRequest) {
        var order = mapper.toOrderLine(orderLineRequest);
        return orderLineRepository.save(order).getId();
    }

    public List<OrderLineResponse> findALLByOrderId(Integer id) {
        return orderLineRepository.findAllByOrderId(id)
                .stream()
                .map(mapper::toOrderLineResponse)
                .collect(Collectors.toList());
    }
}
