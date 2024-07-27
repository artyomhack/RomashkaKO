package com.artyom.romashkako.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public class ValidationException extends RestException{

    private final Map<String, String> errors;

    public ValidationException(Map<String, String> errors) {
        super(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase());
        this.errors = errors;
    }

    public ValidationException(String message, Map<String, String> errors) {
        super(HttpStatus.BAD_REQUEST, message);
        this.errors = errors;
    }
}
