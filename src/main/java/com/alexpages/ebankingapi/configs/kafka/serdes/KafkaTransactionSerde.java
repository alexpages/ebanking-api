package com.alexpages.ebankingapi.configs.kafka.serdes;

import com.alexpages.ebankingapi.models.transaction.Transaction;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class KafkaTransactionSerde implements Serde<Transaction> {

    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }

    @Override
    public void close() {
    }

    @Override //Use <T> JsonSerializer
    public Serializer<Transaction> serializer() {
        return new KafkaJsonSerializer<>();
    }

    @Override //Use <T> JsonDeserializer
    public Deserializer<Transaction> deserializer() {
        return new KafkaJsonDeserializer<>(Transaction.class);
    }
}