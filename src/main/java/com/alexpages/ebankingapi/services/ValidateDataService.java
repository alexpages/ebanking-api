package com.alexpages.ebankingapi.services;

import org.springframework.stereotype.Service;

import java.time.Year;

@Service
public class ValidateDataService {

    public boolean validateYear(int year) {
        if (year < 2003 || year > Year.now().getValue()){
            return false;
        }
        return true;
    }

    public boolean validateMonth(int month) {
        if (month < 1 || month > 12){
            return false;
        }
        return true;
    }



}
