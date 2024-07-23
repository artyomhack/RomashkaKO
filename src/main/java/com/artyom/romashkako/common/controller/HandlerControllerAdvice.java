package com.artyom.romashkako.common.controller;

import com.artyom.romashkako.common.exception.dto.ErrorResponse;
import com.artyom.romashkako.common.exception.NotFoundException;
import com.artyom.romashkako.common.exception.ValidationException;
import com.artyom.romashkako.common.exception.mapper.ValidationExceptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
@RequiredArgsConstructor
public class HandlerControllerAdvice {

    private final ValidationExceptionMapper mapper;

    @ExceptionHandler(BindException.class)
    public ErrorResponse handleBindException(BindException exception) {
        ValidationException valException = mapper.getValidationException(exception);
        return new ErrorResponse(
                valException.getStatus(),
                valException.getMessage(),
                valException.getErrors()
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResponse handleNoSuchElementException(NoSuchElementException ex) {
        NotFoundException notFoundException = new NotFoundException(ex.getMessage());
        return new ErrorResponse(
                notFoundException.getStatus(),
                notFoundException.getMessage()
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse handleNotFoundException(NotFoundException ex) {
        return new ErrorResponse(
                ex.getStatus(),
                ex.getMessage()
        );
    }
}
