package com.openpayd.foreignexchange.service;


import com.openpayd.foreignexchange.model.dto.ConversionDto;
import com.openpayd.foreignexchange.model.entity.Conversion;
import com.openpayd.foreignexchange.model.dto.ConversionInput;
import com.openpayd.foreignexchange.model.dto.ExchangeRate;

import java.util.List;

public interface RestService {
    public ExchangeRate getExchangeRate(String sourceCurrency, String targetCurrency);

    public ConversionDto convertSum(ConversionInput input, ExchangeRate exchangeRate);

    public ConversionDto getConversionById(Long id);

    public List<ConversionDto> getConversionsByDate(int year, int month, int day);
}
