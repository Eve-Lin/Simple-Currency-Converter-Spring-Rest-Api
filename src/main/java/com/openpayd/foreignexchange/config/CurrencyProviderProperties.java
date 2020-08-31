package com.openpayd.foreignexchange.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("currency.provider")
@Component
public class CurrencyProviderProperties {
    @Value("${currency.provider.base-url}")
    private String baseUrl;
    @Value("${currency.provider.access-key}")
    private String accessKey;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
}
