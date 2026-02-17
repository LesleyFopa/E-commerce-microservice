package org.lesley.ecommerce.service;

import lombok.RequiredArgsConstructor;
import org.lesley.ecommerce.dtos.PaymentRequest;
import org.lesley.ecommerce.mapper.PaymentMapper;
import org.lesley.ecommerce.kafka.NotificationProducer;
import org.lesley.ecommerce.kafka.PaymentConfirmation;
import org.lesley.ecommerce.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final NotificationProducer notificationProducer;

    public Integer createPayment( PaymentRequest request) {
        var payment = repository.save(mapper.toPayment(request));
        notificationProducer.sendNotification(
                new PaymentConfirmation(
                        request.orderReference(),
                        request.amount(),
                        request.paymentMethod(),
                        request.customer().firstname(),
                        request.customer().lastname(),
                        request.customer().email()
                )
        );
        return payment.getId();
    }
}
