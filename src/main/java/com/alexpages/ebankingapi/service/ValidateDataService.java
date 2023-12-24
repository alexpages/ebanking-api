package com.alexpages.ebankingapi.service;

import org.springframework.stereotype.Service;

import java.time.Year;

@Service
public class ValidateDataService {
	
	public ValidateDataService()
	{
		
	}

    public boolean validateYear(int year) 
    {
    	return (year < 2003 || year > Year.now().getValue());
    }

    public boolean validateMonth(int month) 
    {
        return (month < 1 || month > 12);
    }
}
