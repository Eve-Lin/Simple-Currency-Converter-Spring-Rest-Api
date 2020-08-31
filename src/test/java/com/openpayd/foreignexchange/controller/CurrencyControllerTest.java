package com.openpayd.foreignexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openpayd.foreignexchange.model.dto.ConversionDto;
import com.openpayd.foreignexchange.model.dto.ExchangeRate;
import com.openpayd.foreignexchange.model.entity.Conversion;
import com.openpayd.foreignexchange.repository.ConversionRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CurrencyControllerTest {
    private Conversion conversion1;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ConversionRepo mockedConversionRepo;

    @BeforeEach
    public void setUp() throws ParseException {
        String sDate1 = "2020-08-28";
        Date date1= new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
        conversion1 = new Conversion() {{
            setId(1L);
            setInputAmount(BigDecimal.valueOf(1000));
            setConvertedAmount(BigDecimal.valueOf(840.4000));
            setRate(0.8404);
            setDate(date1);
            setSourceCurrency("usd");
            setTargetCurrency("eur");
        }};

        when(mockedConversionRepo.findById(conversion1.getId())).thenReturn(Optional.of(conversion1));


    }

    @Test
    public void testGetExchangeRateWithValidCurrenciesReturnsStatusCodeOk() throws Exception {
        this.mockMvc.perform(get("/currency/sc/usd/tc/eur")).andExpect(status().isOk());
    }

    @Test
    public void testGetExchangeRateWithInValidCurrenciesReturnsNotFoundStatus() throws Exception {
        this.mockMvc.perform(get("/currency/sc/usdd/tc/blabla")).andExpect(status().isNotFound());
    }
    @Test
    public void testGetConversionsByIdReturnsCorrectStatusCOde() throws Exception {
        this.mockMvc.perform(get("/currency/conversions/id/1")).andExpect(status().isOk());
    }


    @Test
    public void testGetConversionByIdWithNonExistentIdShouldReturnNotFoundStatusCode() throws Exception {
        this.mockMvc.perform(get("/currency/conversions/id/9")).andExpect(status().isNotFound());
    }

    @Test
    public void testGetConversionByExistentIdShouldReturnConversion() throws Exception {
        this.mockMvc.
                perform(get("/currency/conversions/id/{id}", conversion1.getId())).
                andExpect(jsonPath("$.convertedAmount", is(conversion1.getConvertedAmount().doubleValue())));
    }

    @Test
    public void testGetConversionByDateShouldReturnStatusOk() throws Exception {
        String json = objectMapper.writeValueAsString(conversion1);
        this.mockMvc.
                perform(post("/currency/conversions/date").contentType(MediaType.APPLICATION_JSON).
                        content(json).accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk());
    }
}
