package com.example.chatserver.config;

import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final String bootstrapServer = "kafka:9092";
    private final String topic = "logs";
    private final String autoOffsetReset = "earliest";

    @Bean
    public ProducerFactory<String,String> producerFactory(){
        HashMap<String, Object> configurationProperties = new HashMap<>();
        configurationProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);
        configurationProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
            StringSerializer.class);
        configurationProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
            StringSerializer.class);

        return new DefaultKafkaProducerFactory<>(configurationProperties);
    }

    @Bean
    public ConsumerFactory<String,String> consumerFactory(){
        HashMap<String, Object> configurationProperties = new HashMap<>();
        configurationProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);
        configurationProperties.put(ConsumerConfig.GROUP_ID_CONFIG, topic);
        configurationProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        configurationProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class);
        configurationProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(configurationProperties);
    }

    @Bean
    public KafkaTemplate<String,String> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String,String> KafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String,String> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
