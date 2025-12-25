package com.ec.payment.service.services;

import com.ec.payment.service.entities.UserBalance;
import com.ec.payment.service.payloads.OrderValidateStatusDetails;
import com.ec.payment.service.repo.BalanceRepository;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class KafKaConsumerService {
    @Autowired
    private KafKaProducerService kafKaProducerService;

    @Autowired
    private BalanceRepository balanceRepository;

    @Transactional
    @KafkaListener(topics = "order-validated",groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String event) throws ExecutionException, InterruptedException {
        String status;
        OrderValidateStatusDetails orderValidateStatusDetails;
        Gson gson = new Gson();
        orderValidateStatusDetails=gson.fromJson(event, OrderValidateStatusDetails.class);
        double amount = Math.round((orderValidateStatusDetails.getPrice() * orderValidateStatusDetails.getQuantity()) * 100.0) / 100.0;

        UserBalance wallet = balanceRepository.findById(orderValidateStatusDetails.getUserId())
                .orElse(new UserBalance(orderValidateStatusDetails.getUserId(), 0.0));

        if (amount <= 0) {
            this.kafKaProducerService.sendMessage(
                    "{\"orderId\":\"" + orderValidateStatusDetails.getOrderId() + "\"}",
                    "payment-failed"
            );
            return;
        }

            if (wallet.getAmount() >= amount) {

                double newWalletAmount = Math.round((wallet.getAmount() - amount) * 100.0) / 100.0;
                wallet.setAmount(newWalletAmount);
                balanceRepository.save(wallet);

                status = "payment-success";

                String jsonRequestString = "{"
                        + "\"orderId\" : \"" + orderValidateStatusDetails.getOrderId() + "\", "
                        + "\"userId\" : \"" + orderValidateStatusDetails.getUserId() + "\", "
                        + "\"productId\" : \"" + orderValidateStatusDetails.getProductId() + "\", "
                        + "\"quantity\" : \"" + orderValidateStatusDetails.getQuantity() + "\", "
                        + "\"amount\" : \"" + String.format("%.2f", amount) + "\", "
                        + "\"status\" : \"" + status + "\""
                        + "}";

                this.kafKaProducerService.sendMessage(jsonRequestString,status);
            } else {

                status = "payment-failed";

                String jsonRequestString = "{"
                        + "\"orderId\" : \"" + orderValidateStatusDetails.getOrderId() + "\", "
                        + "\"userId\" : \"" + orderValidateStatusDetails.getUserId() + "\", "
                        + "\"productId\" : \"" + orderValidateStatusDetails.getProductId() + "\", "
                        + "\"quantity\" : \"" + orderValidateStatusDetails.getQuantity() + "\", "
                        + "\"amount\" : \"" + String.format("%.2f", amount) + "\", "
                        + "\"status\" : \"" + status + "\""
                        + "}";

                this.kafKaProducerService.sendMessage(jsonRequestString,status);
            }
    }
}

