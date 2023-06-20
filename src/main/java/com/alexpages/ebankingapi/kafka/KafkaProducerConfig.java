package com.alexpages.ebankingapi.kafka;

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
    public Map<String, Object> producerConfig(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, boostrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }


    //The list of transactions should be consumed from a Kafka topic.
    // key - transaction ID -> String
    // value - JSON representation of the transaction -> String
    //Producer factory, will create producers
    @Bean
    public ProducerFactory<String, String> producerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    //way to send messages (second String can be data object, like Transaction)
    @Bean
    public KafkaTemplate<String, String>  kafkaTemplate(
            ProducerFactory<String, String> producerFactory){
        //producer factory from dependency injection
        return new KafkaTemplate<>(producerFactory);
    }

}