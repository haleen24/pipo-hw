package ru.hse.pipo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import ru.hse.pipo.exception.InventoryException;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(e.getStatusCode())
            .body(ErrorResponse.builder(e, e.getStatusCode(), e.getMessage()).build());
    }

    @ExceptionHandler(InventoryException.class)
    public ResponseEntity<ErrorResponse> handleOtherExceptions(InventoryException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(e.getCode().getHttpStatus())
            .body(ErrorResponse.builder(e, e.getCode().getHttpStatus(), e.getDetails()).build());
    }
}
