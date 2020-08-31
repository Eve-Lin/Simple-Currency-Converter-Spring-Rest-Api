package com.openpayd.foreignexchange.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Table(name = "conversion")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Conversion {

    private Long id;
    private String sourceCurrency;
    private String targetCurrency;
    private Double rate;
    private BigDecimal inputAmount;
    private BigDecimal convertedAmount;
    private Date date;

    public Conversion() {
    }

    public Conversion(String sourceCurrency, String targetCurrency, Double rate, BigDecimal inputAmount, BigDecimal convertedAmount, Date date) {
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.inputAmount = inputAmount;
        this.convertedAmount = convertedAmount;
        this.date = date;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @NonNull
    @Column(columnDefinition = "DECIMAL(13,4)")
    public BigDecimal getInputAmount() {
        return inputAmount;
    }

    public void setInputAmount(BigDecimal amount) {
        this.inputAmount = amount;
    }

    @NonNull
    @Column(columnDefinition = "DECIMAL(13,4)")
    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
