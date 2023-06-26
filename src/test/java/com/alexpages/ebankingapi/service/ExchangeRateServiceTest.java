package com.alexpages.ebankingapi.service;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ExchangeRateServiceTest {

    @Mock
    private ExchangeRateService underTest;

    @BeforeEach
    void setUp(){

    }
    ExchangeRateServiceTest(ExchangeRateService underTest) {
        this.underTest = underTest;
    }

//    @Test
//    void itCanGetCurrentExchangeRateBaseUSD() {
//            String rates = "";
//            try{
//                rates = underTest.getCurrentExchangeRateBaseUSD();
//            } catch (Exception e) {
//                fail();
//            }
//            var status = new JSONObject(rates).get("result").toString();
//            assertEquals("success", status);
//        }
//    }
}