package com.alexpages.ebankingapi.config.kafka;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String boostrapServers;             //holds the url

    //Config to pass to the producer config. Object can be transaction
    @Bean
    public Map<String, Object> kafkaProducerConfiguration(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, boostrapServers);
//        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "false");
//        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
//        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 10000);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, String> kafkaProducerFactory(){
        return new DefaultKafkaProducerFactory<>(kafkaProducerConfiguration());
    }

    @Bean //Injection of ProducerFactory
    public KafkaTemplate<String, String>  kafkaTemplate(
            ProducerFactory<String, String> producerFactory){
        return new KafkaTemplate<>(producerFactory);
    }

}