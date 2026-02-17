package org.lesley.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.lesley.ecommerce.dtos.OrderLineResponse;
import org.lesley.ecommerce.service.OrderLineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-lines")
@RequiredArgsConstructor
public class OrderLineController {

    private final OrderLineService orderLineService;

    @GetMapping("/order/{id}")
    public ResponseEntity<List<OrderLineResponse>> findByOrderId(
            @PathVariable Integer id
    ){
        return ResponseEntity.ok(orderLineService.findALLByOrderId(id));
    }

}
