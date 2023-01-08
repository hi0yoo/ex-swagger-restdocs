package com.example.exswaggerrestdocs.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.example.exswaggerrestdocs.controller")
public class CommonControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ApiResponse<MultiValueMap<String, String>>> validateExceptionHandle(ValidationException e) {
        log.error("Validate Exception", e);

        MultiValueMap<String, String> errors = new LinkedMultiValueMap<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errors.add(fieldError.getField(), fieldError.getDefaultMessage());
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.createValidationError(errors));
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse<?>> otherExceptionHandle(RuntimeException e) {
        log.error("Exception", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.createError(e.getMessage()));
    }
}
