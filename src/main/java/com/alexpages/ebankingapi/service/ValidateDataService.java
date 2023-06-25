package com.alexpages.ebankingapi.service;

import org.springframework.stereotype.Service;

@Service
public class ValidateDataService {

//    public boolean validateTransactionDateFormat(Date date){
//        if(){
//            return false;
//        }
////        return true;pattern = "dd-MM-yyyy")
//
//    }

    public boolean validateYear(int year) {
        if (year <2003 || year>2023){
            return false;
        }
        return true;
    }

    public boolean validateMonth(int month) {
        if (month <1 || month>12){
            return false;
        }
        return true;
    }



}
