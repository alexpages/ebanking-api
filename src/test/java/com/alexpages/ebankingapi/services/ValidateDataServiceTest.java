package com.alexpages.ebankingapi.services;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.alexpages.ebankingapi.service.ValidateDataService;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

@SpringBootTest(classes = ValidateDataService.class)
class ValidateDataServiceTest {

    private ValidateDataService validateDataService;
    
	@BeforeEach
	public void setUp() 
	{
		validateDataService = new ValidateDataService();
	}
    
    @Test
    void validateYear() 
    {
        assertTrue(validateDataService.validateYear(2020));
        assertFalse(validateDataService.validateYear(1900));
        assertFalse(validateDataService.validateYear(2025));
    }

    @Test
    void validateMonth() 
    {
        assertTrue(validateDataService.validateMonth(11));
        assertFalse(validateDataService.validateMonth(13));
        assertFalse(validateDataService.validateMonth(0));
    }
}