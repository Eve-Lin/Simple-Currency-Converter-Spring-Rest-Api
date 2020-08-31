package com.openpayd.foreignexchange.service.impl;


import com.openpayd.foreignexchange.config.CurrencyProviderProperties;
import com.openpayd.foreignexchange.exception.ConversionNotFound;
import com.openpayd.foreignexchange.exception.ExchangeRateNotFound;
import com.openpayd.foreignexchange.model.dto.ConversionDto;
import com.openpayd.foreignexchange.model.dto.ConversionInput;
import com.openpayd.foreignexchange.model.dto.ExchangeRate;
import com.openpayd.foreignexchange.model.entity.Conversion;
import com.openpayd.foreignexchange.repository.ConversionRepo;
import com.openpayd.foreignexchange.service.RestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestServiceImpl implements RestService {

    private final RestTemplate restTemplate;
    private final CurrencyProviderProperties currencyProviderProperties;
    private final ModelMapper modelMapper;
    private final ConversionRepo conversionRepo;

    @Autowired
    public RestServiceImpl(RestTemplate restTemplate, CurrencyProviderProperties currencyProviderProperties, ModelMapper modelMapper, ConversionRepo conversionRepo) {
        this.restTemplate = restTemplate;
        this.currencyProviderProperties = currencyProviderProperties;
        this.modelMapper = modelMapper;
        this.conversionRepo = conversionRepo;
    }

    @Override
    public ExchangeRate getExchangeRate(String sourceCurrency, String targetCurrency) {
        StringBuilder sb = new StringBuilder().
                append(currencyProviderProperties.getBaseUrl()).
                append(currencyProviderProperties.getAccessKey()).
                append("&source=").
                append(sourceCurrency).
                append("&currencies=").
                append(targetCurrency);
        String url = sb.toString();
        ExchangeRate exchangeRate = this.restTemplate.getForObject(url, ExchangeRate.class);
        if (exchangeRate.getQuotes() == null) {
            throw new ExchangeRateNotFound("Could not fetch exchange rate.");
        }
        return exchangeRate;
    }

    @Override
    public ConversionDto convertSum(ConversionInput input, ExchangeRate exchangeRate) {
        StringBuilder sb = new StringBuilder();
        sb.append(input.getSourceCurrency());
        sb.append(input.getTargetCurrency());
        String rateKey = sb.toString();
        Double rate = exchangeRate.getQuotes().entrySet().iterator().next().getValue().doubleValue();
        BigDecimal convertedSum = input.getAmount().multiply(BigDecimal.valueOf(rate));
        Conversion conversion = new Conversion(input.getSourceCurrency(), input.getTargetCurrency(), rate, input.getAmount(), convertedSum, new Date());
        conversionRepo.save(conversion);
        ConversionDto conversionDto = this.modelMapper.map(conversion, ConversionDto.class);
        return conversionDto;
    }

    @Override
    public ConversionDto getConversionById(Long id) {
        Conversion conversion = conversionRepo.findById(id).orElseThrow(() ->
                new ConversionNotFound(String.format("Conversion with id %d could not be found", id)));
        ConversionDto conversionDto = this.modelMapper.map(conversion, ConversionDto.class);
        return conversionDto;
    }

    @Override
    public List<ConversionDto> getConversionsByDate(int year, int month, int day) {
        return conversionRepo.findAllByYearAndMonthAndDay(year, month, day).stream().map(conversion -> {
            ConversionDto conversionDto = this.modelMapper.map(conversion, ConversionDto.class);
            return conversionDto;
        }).collect(Collectors.toList());
    }
}
