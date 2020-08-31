package com.openpayd.foreignexchange.exception;

public class Error {
    private String error;
    private String message;

    public String getError() {
        return error;
    }

    public void setError(String errorName) {
        this.error = errorName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
