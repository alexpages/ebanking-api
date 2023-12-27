package com.alexpages.ebankingapi.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alexpages.ebankingapi.service.ValidateDataService;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;

@ExtendWith(MockitoExtension.class)
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
		boolean value = (2020 < 2003 );  	
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