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
	//send message
	@Bean
	CommandLineRunner commandLineRunner(KafkaTemplate<String, String> kafkaTemplate) {
		String jsonToSend = "{\"id\":\"8333\"," +
				"\"currency\":\"GBP\"," +
				"\"amount\":\"100\"," +
				"\"iban\":\"CH93-0000-0000-0000-0000-0\"," +
				"\"valueDate\":\"01-10-2020\"," +
				"\"description\":\"Online payment GBP\"}";

		return args -> {kafkaTemplate.send("transaction", jsonToSend);
		};
	}
}


