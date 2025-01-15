package org.ms.currencyservice.client;

import org.ms.currencyservice.dto.CurrencyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "freeCurrencyApi", url = "https://api.freecurrencyapi.com/v1")
public interface FreeCurrencyApiClient {
	 @GetMapping("/currencies")
	    CurrencyResponse getCurrencies(@RequestParam("apikey") String apiKey);

	    @GetMapping("/latest")
	    Map<String, Object> getLatestRates(
	            @RequestParam("apikey") String apiKey,
	            @RequestParam("base_currency") String baseCurrency
	    );

	    @GetMapping("/convert")
	    Map<String, Object> convertCurrency(
	            @RequestParam("apikey") String apiKey,
	            @RequestParam("from") String from,
	            @RequestParam("to") String to,
	            @RequestParam("amount") Double amount
	    );
}
