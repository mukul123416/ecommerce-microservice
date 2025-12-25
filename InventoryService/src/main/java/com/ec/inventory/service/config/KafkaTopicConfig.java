package com.ec.inventory.service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic OrderValidated(){
        return TopicBuilder.name("order-validated")
                .build();
    }
    @Bean
    public NewTopic OrderRejected(){
        return TopicBuilder.name("order-rejected")
                .build();
    }
}
