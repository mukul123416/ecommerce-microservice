package com.ec.payment.service.services;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    @Qualifier("PaymentSuccess")
    private NewTopic paymentSuccessTopic;

    @Autowired
    @Qualifier("PaymentFailed")
    private NewTopic paymentFailedTopic;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public KafKaProducerService(
            @Qualifier("PaymentSuccess") NewTopic paymentSuccessTopic,
            @Qualifier("PaymentFailed") NewTopic paymentFailedTopic,
            KafkaTemplate<String, String> kafkaTemplate) {
        this.paymentSuccessTopic = paymentSuccessTopic;
        this.paymentFailedTopic = paymentFailedTopic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String event,String status) throws ExecutionException, InterruptedException {
        String targetTopic;
        if ("payment-success".equalsIgnoreCase(status)) {
            targetTopic = paymentSuccessTopic.name();
        } else {
            targetTopic = paymentFailedTopic.name();
        }
        logger.info(String.format("Sending event => %s", event.toString()));
        // create Message
        Message<String> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, targetTopic)
                .build();
        kafkaTemplate.send(message);
    }
}
