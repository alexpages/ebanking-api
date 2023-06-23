//package com.alexpages.ebankingapi.config.kafka.tbd;
//
//import com.alexpages.ebankingapi.model.transaction.Transaction;
//import org.apache.kafka.common.serialization.Serde;
//import org.apache.kafka.common.serialization.Serdes;
//import org.apache.kafka.common.utils.Bytes;
//import org.apache.kafka.streams.StreamsBuilder;
//import org.apache.kafka.streams.Topology;
//import org.apache.kafka.streams.kstream.*;
//import org.apache.kafka.streams.state.KeyValueStore;
//import org.springframework.context.annotation.Bean;
//import org.springframework.kafka.support.serializer.JsonSerde;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//public class KafkaTransactionTopology extends Topology{
//
//    public static final String TRANSACTIONS = "transactions";
//    public static final String IBAN = "iban";
//
//    @Bean
//    public static Topology buildTopology() {
//        var transactionJsonSerde = new JsonSerde<>(Transaction.class);
//        var listTransactionJsonSerde = getTransactionListSerde(transactionJsonSerde);
//
//        StreamsBuilder streamsBuilder = new StreamsBuilder();
//
//        final KStream<String, Transaction> transactionStream =
//            streamsBuilder.stream(TRANSACTIONS, Consumed.with(Serdes.String(), transactionJsonSerde));
//
//        KTable<String, List<Transaction>> transactionTable = transactionStream
//                .groupBy((key, value) -> getMonthYear(value.getTime()))
//                .aggregate(
//                        ArrayList::new,
//                        (key, value, aggregate) -> {
//                            if (!aggregate.contains(value.getId())) {
//                                aggregate.add(value);
//                            }
//                            return aggregate;
//                        },
//                        Materialized.<String, List<Transaction>, KeyValueStore<Bytes, byte[]>>as(IBAN)
//                                .withValueSerde(listTransactionJsonSerde)
//                );
//
//        return streamsBuilder.build();
//    }
//
//    //Auxiliary functions
//    private static Serde<List<Transaction>> getTransactionListSerde(JsonSerde<Transaction> innerSerde) {
//        Class<List<Transaction>> listClass = (Class<List<Transaction>>) (Class<?>) ArrayList.class;
//        return new Serdes.ListSerde<>(listClass, innerSerde);
//    }
//
//    private static String getMonthYear(String valueDate) {
//        // Extract month and year from the valueDate field and return as a string in the format "MM-yyyy"
//        // Example implementation:
//        LocalDate date = LocalDate.parse(valueDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
//        return date.format(DateTimeFormatter.ofPattern("MM-yyyy"));
//    }
//}
