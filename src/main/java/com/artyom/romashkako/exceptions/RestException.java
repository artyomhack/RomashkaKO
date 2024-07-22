package com.artyom.romashkako.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class RestException extends RuntimeException {
    private HttpStatus status;
    private String message;

    public RestException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
