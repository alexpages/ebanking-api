package com.alexpages.ebankingapi.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {
    @KafkaListener(
            topics = "transaction",
            groupId = "1"
    )
    void listener(String data){
        System.out.println("Listener received: "+ data);
    }
}
