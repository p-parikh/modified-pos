package com.increff.pos.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
public class ApiException extends Exception {
    private String message;

    private List<String> errorlist;

    public ApiException(String message) {
        super(message);
        this.message = message;
    }

    public ApiException( String message, List<String> errorlist) {
        super(message);
        this.message = message;
        this.errorlist = errorlist;
    }

}
