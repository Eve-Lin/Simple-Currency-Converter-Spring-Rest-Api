package com.openpayd.foreignexchange.exception_hanadler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.openpayd.foreignexchange.exception.ConversionNotFound;
import com.openpayd.foreignexchange.exception.Error;
import com.openpayd.foreignexchange.exception.ExchangeRateNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ExceptionHandlers {
        @ExceptionHandler({ConversionNotFound.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Error> handleConversionException() {
        Error error = new Error();
        error.setError("Conversion Not Found");
        error.setMessage("Conversion with the given id could not be found.");
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ExchangeRateNotFound.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Error> handleExchangeRateException() {
        Error error = new Error();
        error.setError("Exchange Rate Not Found");
        error.setMessage("Exchange rate could not be fetched. Please check source & target currencies. You can find a full list of supported currencies here:https://currencies/url");
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InvalidFormatException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Error> handleInvalidFormatException() {
        Error error = new Error();
        error.setError("Invalid date format");
        error.setMessage("Cannot parse date. Please, use the following date format: yyyy-MM-dd.");
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NumberFormatException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Error> handleNumberFormatException() {
        Error error = new Error();
        error.setError("Invalid number");
        error.setMessage("Please, use a valid number.");
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Error> noHandlerFoundException() {
        Error error = new Error();
        error.setError("Incorrect url");
        error.setMessage("The request could not be completed for this url. Please, make sure you are using a correct url.");
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }
}
