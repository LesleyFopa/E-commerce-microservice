package org.lesley.ecommerce.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.lesley.ecommerce.customer.CustomerClient;
import org.lesley.ecommerce.dtos.OrderLineRequest;
import org.lesley.ecommerce.dtos.OrderRequest;
import org.lesley.ecommerce.dtos.OrderResponse;
import org.lesley.ecommerce.exception.BusinessException;
import org.lesley.ecommerce.kafka.OrderConfirmation;
import org.lesley.ecommerce.kafka.OrderProducer;
import org.lesley.ecommerce.mapper.OrderMapper;
import org.lesley.ecommerce.payment.PaymentClient;
import org.lesley.ecommerce.payment.PaymentRequest;
import org.lesley.ecommerce.product.ProductClient;
import org.lesley.ecommerce.product.PurchaseRequest;
import org.lesley.ecommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;

    public Integer createdOrder( OrderRequest orderRequest) {
        //check the customer using OpenFeign client to the customer service
        var customer = this.customerClient.findById(orderRequest.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order :: Customer not found with id: " + orderRequest.customerId()));

        // purchase the products using the product service (RestTemplate )
        var purchaseProducts = this.productClient.purchaseProducts(orderRequest.products());

        //persist the order in the database
        var order = this.orderRepository.save(mapper.toOrder(orderRequest));

        //persist the order lines in the database
        for (PurchaseRequest purchaseRequest : orderRequest.products()) {
           orderLineService.saveOrderLine(
                   new OrderLineRequest(
                           null,
                           order.getId(),
                           purchaseRequest.productId(),
                           purchaseRequest.quantity()
                   )
           );
        }

        // start payment process using the payment service
        var paymentRequest = new PaymentRequest(
                null,
                orderRequest.amount(),
                orderRequest.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);

        //sent the order confirmation email using the notification service(kafka)
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        orderRequest.reference(),
                        orderRequest.amount(),
                        orderRequest.paymentMethod(),
                        customer ,
                        purchaseProducts
                )
        );

        return  order.getId();
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(mapper::fromOrder)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer id) {
        return orderRepository.findById(id)
                .map(mapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
    }
}
