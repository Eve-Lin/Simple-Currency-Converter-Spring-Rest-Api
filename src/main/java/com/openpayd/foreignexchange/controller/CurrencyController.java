package com.openpayd.foreignexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openpayd.foreignexchange.model.dto.ConversionDto;
import com.openpayd.foreignexchange.model.entity.Conversion;
import com.openpayd.foreignexchange.model.dto.ConversionInput;
import com.openpayd.foreignexchange.model.dto.ExchangeRate;
import com.openpayd.foreignexchange.service.RestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/currency")
@Slf4j
public class CurrencyController {
    private final RestService restService;

    @Autowired
    public CurrencyController(RestService restService) {
        this.restService = restService;
    }

    @GetMapping("/sc/{sourceCurrency}/tc/{targetCurrency}")
    public ResponseEntity<ExchangeRate> getExchangeRate(@PathVariable String sourceCurrency, @PathVariable String targetCurrency) {
        ExchangeRate rate = restService.getExchangeRate(sourceCurrency, targetCurrency);
        return new ResponseEntity<>(rate, HttpStatus.OK);
    }

    @PostMapping("/convert")
    public ResponseEntity<ConversionDto> convertSum(@RequestBody ConversionInput input) {
        ExchangeRate exchangeRate = restService.getExchangeRate(input.getSourceCurrency(), input.getTargetCurrency());
        ConversionDto output = restService.convertSum(input, exchangeRate);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @GetMapping("/conversions/id/{id}")
    public ResponseEntity<ConversionDto> getConversionById(@PathVariable Long id) {
        ConversionDto output = restService.getConversionById(id);

        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @PostMapping("/conversions/date")
    public ResponseEntity<List<ConversionDto>> getConversionByDate(@RequestBody @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Conversion conversion) {
        LocalDate localDate = conversion.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        List<ConversionDto> output = restService.getConversionsByDate(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        return new ResponseEntity<>(output, HttpStatus.OK);
    }
}
