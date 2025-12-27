package com.ec.payment.service.services;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class KafKaProducerService {

    private static final Logger logger =
            LoggerFactory.getLogger(KafKaProducerService.class);

    private final NewTopic paymentSuccessTopic;
    private final NewTopic paymentFailedTopic;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafKaProducerService(
            @Qualifier("paymentSuccess") NewTopic paymentSuccessTopic,
            @Qualifier("paymentFailed") NewTopic paymentFailedTopic,
            KafkaTemplate<String, String> kafkaTemplate) {
        this.paymentSuccessTopic = paymentSuccessTopic;
        this.paymentFailedTopic = paymentFailedTopic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String event, String status) {
        String targetTopic;

        if ("payment-success".equalsIgnoreCase(status)) {
            targetTopic = paymentSuccessTopic.name();
        } else {
            targetTopic = paymentFailedTopic.name();
        }

        logger.info("Sending event => {}", event);

        Message<String> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, targetTopic)
                .build();

        kafkaTemplate.send(message);
    }
}