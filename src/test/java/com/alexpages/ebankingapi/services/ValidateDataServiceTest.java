package com.alexpages.ebankingapi.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alexpages.ebankingapi.service.ValidateDataService;

import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class ValidateDataServiceTest {

	@InjectMocks
    private ValidateDataService validateDataService;
    
    @Test
    void validateYear() 
    {
    	assertFalse(validateDataService.validateYear(2020));
    	assertTrue(validateDataService.validateYear(1900));
    	assertTrue(validateDataService.validateYear(2025));
    }

    @Test
    void validateMonth() 
    {
    	assertFalse(validateDataService.validateMonth(11));
    	assertTrue(validateDataService.validateMonth(13));
    	assertTrue(validateDataService.validateMonth(0));
    }
}