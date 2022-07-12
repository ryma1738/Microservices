package com.microservices.currencyconversionservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@RestController
public class CurrencyConversionController {

    @Autowired
    private Environment env;

    @Autowired
    private CurrencyExchangeProxy proxy;

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCC(@PathVariable String from,
                                          @PathVariable String to,
                                          @PathVariable BigDecimal quantity) {
        ResponseEntity<CurrencyConversion> response = new RestTemplate().getForEntity(
                "http://localhost:8000/currency-exchange/from/"+ from +"/to/" + to, CurrencyConversion.class);
        CurrencyConversion currencyObject = response.getBody();

        BigDecimal total = quantity.multiply(currencyObject.getExchangeRate());

        return new CurrencyConversion(currencyObject.getId(), from, to, currencyObject.getExchangeRate(),
                quantity, total, env.getProperty("Local.server.port"));
    }

    //diffrent way to do the same thing using feign
    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateFeign(@PathVariable String from,
                                          @PathVariable String to,
                                          @PathVariable BigDecimal quantity) {
        CurrencyConversion currencyObject = proxy.getExchangeValue(from, to);

        BigDecimal total = quantity.multiply(currencyObject.getExchangeRate());

        return new CurrencyConversion(currencyObject.getId(), from, to, currencyObject.getExchangeRate(),
                quantity, total, env.getProperty("Local.server.port"));
    }
}
