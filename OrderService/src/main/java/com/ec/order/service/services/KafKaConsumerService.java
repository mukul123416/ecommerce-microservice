package com.ec.order.service.services;

import com.ec.order.service.entities.Order;
import com.ec.order.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.order.service.payloads.OrderValidateStatusDetails;
import com.ec.order.service.payloads.PaymentStatusDetails;
import com.ec.order.service.repo.OrderRepository;
import com.google.gson.Gson;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafKaConsumerService {

    private final OrderRepository orderRepository;

    private final Gson gson = new Gson();

    public KafKaConsumerService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @KafkaListener(topics = "payment-success",groupId = "${spring.kafka.consumer.group-id}")
    public void paymentSuccessConsume(String event) throws ResourceNotFoundException {
        PaymentStatusDetails dto = gson.fromJson(event, PaymentStatusDetails.class);
        updateOrderStatus(dto.getOrderId(), "CONFIRMED");
    }
    @KafkaListener(topics = "payment-failed",groupId = "${spring.kafka.consumer.group-id}")
    public void paymentFailedConsume(String event) throws ResourceNotFoundException {
        PaymentStatusDetails dto = gson.fromJson(event, PaymentStatusDetails.class);
        updateOrderStatus(dto.getOrderId(), "FAILED");
    }
    @KafkaListener(topics = "order-validated",groupId = "${spring.kafka.consumer.group-id}")
    public void orderValidatedConsume(String event) throws ResourceNotFoundException {
        OrderValidateStatusDetails dto = gson.fromJson(event, OrderValidateStatusDetails.class);
        updateOrderStatus(dto.getOrderId(), "VALIDATING");
    }
    @KafkaListener(topics = "order-rejected",groupId = "${spring.kafka.consumer.group-id}")
    public void orderRejectedConsume(String event) throws ResourceNotFoundException {
        OrderValidateStatusDetails dto = gson.fromJson(event, OrderValidateStatusDetails.class);
        updateOrderStatus(dto.getOrderId(), "CANCELLED");
    }

    private void updateOrderStatus(Long orderId, String status)
            throws ResourceNotFoundException {

        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found with id : " + orderId));

        order.setStatus(status);
        this.orderRepository.save(order);
    }
}

