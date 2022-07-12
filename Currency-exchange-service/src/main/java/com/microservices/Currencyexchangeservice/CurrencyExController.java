package com.microservices.Currencyexchangeservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CurrencyExController {

    @Autowired
    CurrencyExchangeRep rep;

    @Autowired
    Environment env;

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange getCurrencyExchange(@PathVariable String from, @PathVariable String to) {
        CurrencyExchange ce = rep.findByFromAndTo(from.toUpperCase(), to.toUpperCase());

        if(ce == null) throw new RuntimeException("Unable to find data from " + from + " to " + to);
        ce.setEnvironment(env.getProperty("local.server.port"));
        return ce;
    }
}
