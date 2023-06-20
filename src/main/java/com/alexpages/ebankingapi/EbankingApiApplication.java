package com.alexpages.ebankingapi;

import com.alexpages.ebankingapi.model.transaction.Currency;
import com.alexpages.ebankingapi.model.transaction.Transaction;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class EbankingApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingApiApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(KafkaTemplate<String, String> kafkaTemplate) {
		List<Transaction> transactions = List.of(
				new Transaction(
						"89d3o179-abcd-465b-o9ee-e2d5f63fEld46",
						Currency.GBP,
						160,
						"CH93-0000-0000-0000-0000-0",
						LocalDateTime.of(2020, 1, 2, 2, 3, 3),
						"Online payment GBP"
				));

		return args -> {kafkaTemplate.send("transaction", transactions.get(0).toString());
		};
	}
}


