package com.alexpages.ebankingapi.kafka.config;

import com.alexpages.ebankingapi.kafka.streams.KafkaTransactionTopology;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Properties;

@Configuration
public class KafkaStreamsConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String boostrapServers;             //holds the url

    @Bean
    public Properties kafkaStreamsProperties(){
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "ebanking-api");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, boostrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        return props;
    }

    @Bean
    public KafkaStreams kafkaStreams(Properties kafkaStreamsProperties){
        var topology = KafkaTransactionTopology.buildTopology();
        var kafkaStreams = new KafkaStreams(topology, kafkaStreamsProperties);
        kafkaStreams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
        return kafkaStreams;
    }


}
