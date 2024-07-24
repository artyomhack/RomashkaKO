package com.artyom.romashkako.common.controller;

import com.artyom.romashkako.common.dto.ErrorResponse;
import com.artyom.romashkako.common.exception.InternalServerError;
import com.artyom.romashkako.common.exception.NotFoundException;
import com.artyom.romashkako.common.exception.ValidationException;
import com.artyom.romashkako.common.mapper.ValidationExceptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdviceController {

    @Autowired
    private ValidationExceptionMapper mapper;

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException exception) {
        ValidationException valException = mapper.getValidationException(exception);
        ErrorResponse errorResponse = new ErrorResponse(
                valException.getStatus(),
                valException.getMessage(),
                valException.getErrors()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {
        InternalServerError serverError = new InternalServerError(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                serverError.getStatus(),
                serverError.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getStatus(),
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
