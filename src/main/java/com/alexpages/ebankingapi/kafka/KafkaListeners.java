package com.alexpages.ebankingapi.kafka;

import com.alexpages.ebankingapi.model.transaction.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {
    @KafkaListener(
            topics = "transaction",
            groupId = "1"
    )
    void listener(String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Transaction transaction = objectMapper.readValue(data, Transaction.class);
        System.out.println("Listener received: "+ transaction.toString());
    }
}
