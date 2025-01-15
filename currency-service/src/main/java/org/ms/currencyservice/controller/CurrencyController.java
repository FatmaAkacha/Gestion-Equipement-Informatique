package org.ms.currencyservice.controller;

import org.ms.currencyservice.dto.CurrencyResponse;
import org.ms.currencyservice.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping
    public CurrencyResponse getAllCurrencies() {
        return currencyService.getAllCurrencies();
    }

    @GetMapping("/rates")
    public Map<String, Object> getExchangeRates(@RequestParam String baseCurrency) {
        return currencyService.getExchangeRates(baseCurrency);
    }

    @GetMapping("/convert")
    public Double convertCurrency(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam Double amount) {
        return currencyService.convertCurrency(from, to, amount);
    }
}
