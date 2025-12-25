package com.ec.inventory.service.services;

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
    @Qualifier("OrderValidated")
    private NewTopic validatedTopic;

    @Autowired
    @Qualifier("OrderRejected")
    private NewTopic rejectedTopic;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public KafKaProducerService(
            @Qualifier("OrderValidated") NewTopic validatedTopic,
            @Qualifier("OrderRejected") NewTopic rejectedTopic,
            KafkaTemplate<String, String> kafkaTemplate) {
        this.validatedTopic = validatedTopic;
        this.rejectedTopic = rejectedTopic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String event,String status) throws ExecutionException, InterruptedException {
        String targetTopic;
        if ("validated".equalsIgnoreCase(status)) {
            targetTopic = validatedTopic.name();
        } else {
            targetTopic = rejectedTopic.name();
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
