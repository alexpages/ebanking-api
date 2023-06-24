package com.alexpages.ebankingapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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
    private ObjectMapper objectMapper = new ObjectMapper();

    public String getCurrentExchangeRateBaseUSD() {
        String string_url = "https://api.currencyfreaks.com/v2.0/rates/latest?apikey=" + api_key;
        try {
            //Connect to URL
            URL url = new URL(string_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int httpResponseCode = connection.getResponseCode();

            //Read response
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
            //TODO -> throw a custom exception, etc.)
            e.printStackTrace();
        }
        return null;
    }
}