package com.alexpages.ebankingapi.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class RequestControlService {

    private final ClientDataService clientDataService;

    public boolean validateYearAndMonth(int year, int month) {
        try {
            YearMonth yearMonth = YearMonth.of(year, month);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean clientIsPresent(String client){
        try{
            clientIsPresent(client);
            return true;
        } catch (Exception e){
            return false;
        }
    }




}
