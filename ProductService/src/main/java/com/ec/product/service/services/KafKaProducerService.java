package com.ec.product.service.services;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final NewTopic topic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafKaProducerService(NewTopic topic, KafkaTemplate<String, String> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String event) {
        logger.info("Sending event => {}", event);
        // create Message
        Message<String> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, this.topic.name())
                .build();
        this.kafkaTemplate.send(message);
    }
}
