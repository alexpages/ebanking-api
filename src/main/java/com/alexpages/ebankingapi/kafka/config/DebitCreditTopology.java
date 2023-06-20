package com.alexpages.ebankingapi.kafka.config;

import com.alexpages.ebankingapi.model.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.springframework.kafka.support.serializer.JsonSerde;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebitCreditTopology {


    public static Topology buildTopology(){
        Serde<Transaction> transactionSerdes = new JsonSerde<>(Transaction.class);
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        KStream<String, >

    }
}
