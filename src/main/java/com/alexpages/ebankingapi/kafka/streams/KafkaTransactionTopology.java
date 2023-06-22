package com.alexpages.ebankingapi.kafka.streams;

import com.alexpages.ebankingapi.model.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.apache.kafka.streams.state.WindowStore;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.ArrayList;
import java.util.List;

public class KafkaTransactionTopology extends Topology{

    public static final String TRANSACTIONS = "transactions";
    public static final String IBAN = "iban";

    @Bean
    public static Topology buildTopology() {
        var transactionJsonSerde = new JsonSerde<>(Transaction.class);
        var listTransactionJsonSerde = new Serdes.ListSerde<Transaction>();
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        final KStream<String, Transaction> transactionStream =
            streamsBuilder.stream(TRANSACTIONS, Consumed.with(Serdes.String(), transactionJsonSerde));

        KTable<String, List<Transaction>> transactionTable = transactionStream
                .groupBy((key, value) -> value.getIban())
                .aggregate(
                        ArrayList::new,
                        (key, value, aggregate) -> {
                            aggregate.add(value);
                            return aggregate;
                        },
                        Materialized.<String, List<Transaction>, KeyValueStore<Bytes, byte[]>>as(IBAN)
                                .withValueSerde(listTransactionJsonSerde)
                );
        return streamsBuilder.build();
    }
}








//    //Create Stream
//    final KStream<String, Transaction> transactionKStream =
//            streamsBuilder.stream(TRANSACTIONS, Consumed.with(Serdes.String(), transactionJsonSerde));
//
//        transactionKStream.groupBy((key, value) -> KeyValue.pair(value.getIban(), value)); //Group by IBAN so it can be retrieved
//                transactionKStream.to(IBAN, Produced.with(Serdes.String(), transactionJsonSerde));