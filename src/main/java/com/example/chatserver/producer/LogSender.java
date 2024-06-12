package com.example.chatserver.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogSender {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic = "logs";

    public void sendMessage(String message){
        kafkaTemplate.send(topic, message);
    }
}
