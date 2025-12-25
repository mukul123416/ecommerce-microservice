package com.ec.inventory.service.services;

import com.ec.inventory.service.entities.Inventory;
import com.ec.inventory.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.inventory.service.payloads.OrderPriceCalDetails;
import com.ec.inventory.service.payloads.PaymentStatusDetails;
import com.ec.inventory.service.repo.InventoryRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class KafKaConsumerService {
    @Autowired
    private KafKaProducerService kafKaProducerService;
    @Autowired
    private InventoryRepository inventoryRepository;

    private final Gson gson = new Gson();

    @KafkaListener(topics = "order-price-calculated",groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String event) throws ExecutionException, InterruptedException {
        String status;
        OrderPriceCalDetails orderPriceCalDetails;
        orderPriceCalDetails=gson.fromJson(event, OrderPriceCalDetails.class);
        Long productId = orderPriceCalDetails.getProductId();
        Inventory byProductId = inventoryRepository.findByProductId(productId);
        if (byProductId!=null) {
            if (orderPriceCalDetails.getQuantity()<=byProductId.getQuantity()) {
                status = "validated";
                String jsonRequestString = "{"
                        + "\"userId\" : \"" + orderPriceCalDetails.getUserId() + "\", "
                        + "\"orderId\" : \"" + orderPriceCalDetails.getOrderId() + "\", "
                        + "\"status\" : \"validated\", "
                        + "\"reason\" : \"Stock available\", "
                        + "\"price\" : \"" + orderPriceCalDetails.getPrice() + "\", "
                        + "\"productId\" : \"" + orderPriceCalDetails.getProductId() + "\", "
                        + "\"quantity\" : \"" + orderPriceCalDetails.getQuantity() + "\""
                        + "}";

                this.kafKaProducerService.sendMessage(jsonRequestString,status);
            } else {
                status = "rejected";

                String jsonRequestString = "{"
                        + "\"userId\" : \"" + orderPriceCalDetails.getUserId() + "\", "
                        + "\"orderId\" : \"" + orderPriceCalDetails.getOrderId() + "\", "
                        + "\"status\" : \"rejected\", "
                        + "\"reason\" : \"Insufficient stock\", "
                        + "\"price\" : \"" + orderPriceCalDetails.getPrice() + "\", "
                        + "\"productId\" : \"" + orderPriceCalDetails.getProductId() + "\", "
                        + "\"quantity\" : \"" + orderPriceCalDetails.getQuantity() + "\""
                        + "}";

                this.kafKaProducerService.sendMessage(jsonRequestString,status);
            }
        }
    }

    @KafkaListener(topics = "payment-success",groupId = "${spring.kafka.consumer.group-id}")
    public void paymentSuccessConsume(String event) throws ResourceNotFoundException {
        PaymentStatusDetails dto = gson.fromJson(event, PaymentStatusDetails.class);
        updateInventoryQuantity(dto.getProductId(), dto.getQuantity());
    }

    private void updateInventoryQuantity(Long productId, Integer quantity)
            throws ResourceNotFoundException {

        Inventory inventory = inventoryRepository.findByProductId(productId);
        if (inventory!=null) {
            int updatedQuantity = inventory.getQuantity() - quantity;
            inventory.setQuantity(updatedQuantity);
            inventoryRepository.save(inventory);
        }
    }
}

