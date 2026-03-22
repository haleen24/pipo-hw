package ru.hse.pipo.controller;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(ErrorResponse.builder(e, e.getStatusCode(), e.getMessage()).build());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleOtherExceptions(RuntimeException e) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder(e, HttpStatusCode.valueOf(INTERNAL_SERVER_ERROR.value()), e.getMessage()).build());
    }
}
