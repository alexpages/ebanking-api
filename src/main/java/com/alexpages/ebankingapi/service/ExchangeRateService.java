package com.alexpages.ebankingapi.service;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alexpages.ebankingapi.error.EbankingManagerException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Service
public class ExchangeRateService {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateService.class);

    @Value("${EXCHANGE_RATE_KEY}")
    private String EXCHANGE_RATE_KEY;

    @Value("${EXCHANGE_RATE_STRING_URL}")
    private String EXCHANGE_RATE_STRING_URL;

    public String getCurrentExchangeRateBaseUSD() 
    {
    	String string_url = EXCHANGE_RATE_STRING_URL + EXCHANGE_RATE_KEY;
        try {
            URL url = new URL(string_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            StringBuilder sb = new StringBuilder();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String strCurrentLine = br.readLine();
                while ((strCurrentLine !=null)){
                    sb.append(strCurrentLine);
                    strCurrentLine = br.readLine();
                }
                return sb.toString();
            }
        } catch (IOException e) {
            logger.error("Failed to fetch external exchange rate api");
            throw new EbankingManagerException("Failed to fetch external exchange rate api");
        }
        return null;
    }
}