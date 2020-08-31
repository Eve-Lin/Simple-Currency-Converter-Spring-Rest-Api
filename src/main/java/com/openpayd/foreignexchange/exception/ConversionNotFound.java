package com.openpayd.foreignexchange.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ConversionNotFound extends RuntimeException {
    public ConversionNotFound(String message){
        super(message);
    }
}
