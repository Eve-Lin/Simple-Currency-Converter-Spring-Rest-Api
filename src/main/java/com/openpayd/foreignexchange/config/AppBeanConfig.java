package com.openpayd.foreignexchange.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppBeanConfig {

//    @Bean
//    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//        return builder.build();
//    }
@Bean
public RestTemplate restTemplate() {
    return new RestTemplate();
}

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
