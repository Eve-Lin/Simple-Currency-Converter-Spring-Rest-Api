package com.openpayd.foreignexchange.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openpayd.foreignexchange.config.AppBeanConfig;
import com.openpayd.foreignexchange.config.CurrencyProviderProperties;
import com.openpayd.foreignexchange.exception.ConversionNotFound;
import com.openpayd.foreignexchange.model.dto.ConversionInput;
import com.openpayd.foreignexchange.model.dto.ExchangeRate;
import com.openpayd.foreignexchange.model.entity.Conversion;
import com.openpayd.foreignexchange.repository.ConversionRepo;
import com.openpayd.foreignexchange.service.impl.RestServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppBeanConfig.class, CurrencyProviderProperties.class})
public class RestServiceTest {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CurrencyProviderProperties currencyProviderProperties = new CurrencyProviderProperties();
    @Autowired
    private ModelMapper modelMapper;
    private ConversionInput conversionInput;
    private Conversion conversion1;
    private ExchangeRate exchangeRate;
    private ConversionRepo mockedConversionRepo = Mockito.mock(ConversionRepo.class);
    private RestService restService;
    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();


//    @Test
//    public void getExchangeRateWithValidCurrenciesShouldReturnExchangeRate() {
//        RestService restService = new RestServiceImpl(this.restTemplate, this.currencyProviderProperties, this.modelMapper, this.mockedConversionRepo);
//        ExchangeRate exchangeRate = restService.getExchangeRate("usd", "eur");
//        assertTrue(exchangeRate.getQuotes() != null);
//    }

    @Before
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Before
    public void setConversion(){
        conversion1 = new Conversion() {{
            setId(1L);
            setInputAmount(BigDecimal.valueOf(1000));
            setConvertedAmount(BigDecimal.valueOf(840.4000));
            setRate(0.8404);
            try {
                setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-08-28"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            setSourceCurrency("usd");
            setTargetCurrency("eur");
        }};
    }

    ExchangeRate setExchangeRate() {
        exchangeRate = new ExchangeRate();
        Map<String, Number> rate = new HashMap<>();
        rate.put("usdeur", 0.8404);
        exchangeRate.setQuotes(rate);
        return exchangeRate;
    }

    ConversionInput setConversionInput() {
        conversionInput = new ConversionInput();
        conversionInput.setAmount(BigDecimal.valueOf(1000));
        conversionInput.setSourceCurrency("usd");
        conversionInput.setTargetCurrency("eur");
        return conversionInput;
    }

    @Test
    public void getConversionWithExistentIdShouldReturnConversion() {
        RestService restService = new RestServiceImpl(this.restTemplate, this.currencyProviderProperties, this.modelMapper, this.mockedConversionRepo);
        when(mockedConversionRepo.findById(conversion1.getId())).thenReturn(Optional.of(conversion1));
        Conversion actualConversion = this.modelMapper.map(restService.getConversionById(conversion1.getId()), Conversion.class);
        assertEquals(BigDecimal.valueOf(840.4000), actualConversion.getConvertedAmount());
    }

    @Test
    public void convertSumShouldConvertSum() {
        RestService restService = new RestServiceImpl(this.restTemplate, this.currencyProviderProperties, this.modelMapper, this.mockedConversionRepo);
        conversionInput = setConversionInput();
        exchangeRate = setExchangeRate();
        Conversion conversion = this.modelMapper.map(restService.convertSum(conversionInput, exchangeRate), Conversion.class);
        assertEquals(0,BigDecimal.valueOf(840.4000).compareTo(conversion.getConvertedAmount()));

    }

    @Test
    public void getConversionWithNonExistentIdShouldThrowConversionNotFound() {
        long id = -1;
        RestService restService = new RestServiceImpl(this.restTemplate, this.currencyProviderProperties, this.modelMapper, this.mockedConversionRepo);
        assertThrows(ConversionNotFound.class, () -> restService.getConversionById(id));
    }

    @Test
    public void getConversionsByExistentDateSHouldReturnListOfConversions() {
        RestService restService = new RestServiceImpl(this.restTemplate, this.currencyProviderProperties, this.modelMapper, this.mockedConversionRepo);
        List<Conversion> expectedConversions = new ArrayList<>() {{
            add(conversion1);
        }};
        when(mockedConversionRepo.findAllByYearAndMonthAndDay(conversion1.getDate().getYear(), conversion1.getDate().getMonth(), conversion1.getDate().getDay())).thenReturn(expectedConversions);
        List<Conversion> actualConversions = restService.getConversionsByDate(conversion1.getDate().getYear(), conversion1.getDate().getMonth(), conversion1.getDate().getDay()).stream().map(conversion -> {
            Conversion conversionDto = this.modelMapper.map(conversion, Conversion.class);
            return conversionDto;
        }).collect(Collectors.toList());
        assertEquals(expectedConversions.size(), actualConversions.size());
    }
}
