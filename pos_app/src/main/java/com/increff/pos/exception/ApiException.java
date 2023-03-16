package com.increff.pos.exception;

import org.springframework.validation.FieldError;

import java.util.List;

public class ApiException extends Exception {
    private String message;

    private Integer statusCode;

    private List<String> errorlist;

    public ApiException(String message) {
        super(message);
        this.message = message;
    }

    public ApiException(Integer statusCode, String message, List<String> errorlist) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
        this.errorlist = errorlist;
    }

    public ApiException() {
        super();
    }
}
