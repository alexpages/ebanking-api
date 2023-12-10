package com.alexpages.ebankingapi.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alexpages.ebankingapi.service.ExchangeRateService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ExchangeRateService.class)
class ExchangeRateServiceTest {

    @Autowired
    private ExchangeRateService underTest;

    @Test
    void itShouldGetCurrentExchangeRate() {
        // Given

        // When
        String rates = underTest.getCurrentExchangeRateBaseUSD();

        // Then
        assertNotNull(rates);
    }


}