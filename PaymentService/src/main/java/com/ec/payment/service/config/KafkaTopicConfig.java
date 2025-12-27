package com.ec.payment.service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic paymentSuccess(){
        return TopicBuilder.name("payment-success")
                .build();
    }
    @Bean
    public NewTopic paymentFailed(){
        return TopicBuilder.name("payment-failed")
                .build();
    }
}
