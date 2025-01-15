package org.ms.currencyservice.service;

import org.ms.currencyservice.client.FreeCurrencyApiClient;
import org.ms.currencyservice.dto.CurrencyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CurrencyService {
    private static final String API_KEY = "fca_live_UXt6ThoflfaqyjtQ0zlhvbDuIbujSheZIYGR3R5Q"; 

    @Autowired
    private FreeCurrencyApiClient freeCurrencyApiClient;

    public CurrencyResponse getAllCurrencies() {
        CurrencyResponse response = freeCurrencyApiClient.getCurrencies(API_KEY);
        System.out.println("API Response: " + response);  // Affiche la réponse brute
        if (response != null && response.getData() != null) {
            System.out.println("Currencies: " + response.getData());
        } else {
            System.out.println("Empty or null response from API");
        }
        return response;
    }



    public Map<String, Object> getExchangeRates(String baseCurrency) {
        return freeCurrencyApiClient.getLatestRates(API_KEY, baseCurrency);
    }

    public Double convertCurrency(String from, String to, Double amount) {
        Map<String, Object> response = freeCurrencyApiClient.getLatestRates(API_KEY, from);
        // Supposons que la réponse contient un taux pour la devise 'to'
        Map<String, Double> rates = (Map<String, Double>) response.get("data");
        Double conversionRate = rates.get(to);  // Récupère le taux de conversion vers 'to'
        
        if (conversionRate != null) {
            return amount * conversionRate;  // Effectue la conversion
        } else {
            throw new IllegalArgumentException("Conversion rate not found for " + to);
        }
    }

}