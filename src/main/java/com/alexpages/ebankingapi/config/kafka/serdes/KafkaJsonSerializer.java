package com.alexpages.ebankingapi.config.kafka.serdes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

@Slf4j
@NoArgsConstructor
public class KafkaJsonSerializer<T> implements Serializer<T> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }

    @Override
    public byte[] serialize(String topic, T o) {
        byte[] byteValues = null;
        try {
            byteValues = objectMapper.writeValueAsBytes(o);
        } catch (JsonProcessingException e) {
            log.error(e.toString());
        }
        return byteValues;
    }

    @Override
    public void close() {
    }
}