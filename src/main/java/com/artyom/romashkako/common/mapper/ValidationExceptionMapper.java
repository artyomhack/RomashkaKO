package com.artyom.romashkako.common.mapper;

import com.artyom.romashkako.common.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ValidationExceptionMapper {
    public ValidationException getValidationException(BindException bindException) {
        List<FieldError> fieldErrors = bindException.getFieldErrors();
        Map<String, String> errors = fieldErrors.stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                ));
        return new ValidationException(errors);
    }

}
