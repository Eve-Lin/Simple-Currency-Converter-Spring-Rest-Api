package com.openpayd.foreignexchange.model.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRate {
    private Map<String, Number> quotes;

    @JsonAnyGetter
    public Map<String, Number> getQuotes() {
        return quotes;
    }

    public void setQuotes(Map<String, Number> quotes) {
        this.quotes = quotes;
    }
}
