package org.ms.facture_servicePJ.feign;


import org.ms.facture_servicePJ.model.CurrencyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "currency-service", url = "http://localhost:8084/api/currencies")
public interface CurrencyServiceClient {

    // Récupérer toutes les devises
    @GetMapping
    CurrencyResponse getAllCurrencies();
    
    // Récupérer les taux de conversion de la devise de base
    @GetMapping("/rates")
    Map<String, Object> getExchangeRates(@RequestParam("baseCurrency") String baseCurrency);

    // Effectuer la conversion entre deux devises
    @GetMapping("/convert")
    Double convertCurrency(@RequestParam("from") String from, 
                           @RequestParam("to") String to, 
                           @RequestParam("amount") Double amount);
}
