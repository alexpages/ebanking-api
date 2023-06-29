package com.alexpages.ebankingapi.configs.kafka.serdes;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

@Slf4j
@NoArgsConstructor
public class KafkaJsonDeserializer<T> implements Deserializer<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Class<T> type;

    public KafkaJsonDeserializer(Class<T> type) {
        this.type = type;
    }

    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }

    @Override
    public T deserialize(String s, byte[] bytes) {
        try {
            return objectMapper.readValue(bytes, type);
        } catch (IOException e) {
            log.warn("Could not deserialize for type {}", this.type.getName());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() {
    }
}