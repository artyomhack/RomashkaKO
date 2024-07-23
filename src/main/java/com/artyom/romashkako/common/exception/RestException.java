package com.artyom.romashkako.common.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class RestException extends RuntimeException {
    private int code;
    private HttpStatus status;
    private String message;

    public RestException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
        this.code = status.value();
    }
}
