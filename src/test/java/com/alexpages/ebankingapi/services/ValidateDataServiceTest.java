package com.alexpages.ebankingapi.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = ValidateDataService.class)
class ValidateDataServiceTest{

    @Autowired
    private ValidateDataService underTest;

    @Test
    void yearShouldNotBeCorrect() {
        // Given
        int testYear = 1900;
        int testYear2 = 2024;

        // When
        boolean result = underTest.validateYear(testYear);
        boolean result2 = underTest.validateYear(testYear2);

        // Then
        assertFalse(result);
        assertFalse(result);
    }

    @Test
    void yearShouldBeCorrect() {
        // Given
        int testYear = 2020;

        // When
        boolean result = underTest.validateYear(testYear);

        // Then
        assertTrue(result);
    }

    @Test
    void monthShouldBeCorrect() {
        // Given
        int testMonth = 12;

        // When
        boolean result = underTest.validateMonth(testMonth);

        // Then
        assertTrue(result);
    }

    @Test
    void monthShouldNotBeCorrect() {
        // Given
        int testMonth = 13;
        int testMonth2 = 10;

        // When
        boolean result = underTest.validateMonth(testMonth);
        boolean result2 = underTest.validateMonth(testMonth);

        // Then
        assertFalse(result);
        assertFalse(result2);
    }

}