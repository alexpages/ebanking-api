package com.alexpages.ebankingapi.api;

import com.alexpages.ebankingapi.kafka.streams.KafkaTransactionTopology;
import com.alexpages.ebankingapi.model.transaction.Transaction;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiService {

    @Autowired
    private KafkaStreams kafkaStreams;
    private String iban = "CH93-0000-0000-0000-0000-0";

    @Autowired
    public ApiService(KafkaStreams kafkaStreams) {
        this.kafkaStreams = kafkaStreams;
    }

    public Transaction getTransactionsByEmail(){
        return getStore().get(this.iban);
    }


    private ReadOnlyKeyValueStore<String, Transaction> getStore(){
        return kafkaStreams.store(
                StoreQueryParameters.fromNameAndType(
                        KafkaTransactionTopology.IBAN,
                        QueryableStoreTypes.keyValueStore()
                )
        );
    }

}
