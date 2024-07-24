package com.artyom.romashkako.common.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@Setter
public class ErrorResponse {
    private int code;
    private String status;
    private String message;
    private Map<String, String> errors;

    public ErrorResponse(HttpStatus status, String message) {
        this(status, message, null);
    }

    public ErrorResponse(HttpStatus status, String message, Map<String, String> errors) {
        this.status = status.name();
        this.message = message;
        this.errors = errors;
        this.code = status.value();
    }

}
