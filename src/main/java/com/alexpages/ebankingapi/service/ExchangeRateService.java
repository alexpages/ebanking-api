package com.alexpages.ebankingapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    @Value("${EXCHANGE_RATE_KEY}")
    private String api_key;
    private HttpURLConnection httpURLConnection;


    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateService.class);

    public String getCurrentExchangeRateBaseUSD() {
        String string_url = "https://api.currencyfreaks.com/v2.0/rates/latest?apikey=" + api_key;
        try {
            // Connect to URL
            URL url = new URL(string_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int httpResponseCode = connection.getResponseCode();

            // Read
            StringBuilder sb = new StringBuilder();
            if (httpResponseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String strCurrentLine = br.readLine();
                while ((strCurrentLine !=null)){
                    sb.append(strCurrentLine);
                    strCurrentLine = br.readLine();
                }
                return sb.toString();
            }
        } catch (IOException e) {
            // Log
            logger.error("Failed to fetch external exchange rate api");
            throw new RuntimeException(e);
        }
        return null;
    }
}