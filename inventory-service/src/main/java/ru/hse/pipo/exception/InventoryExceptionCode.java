package ru.hse.pipo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum InventoryExceptionCode {
    SHIPMENT_NOT_FOUND("INV-001", "Shipment with id {%s} not found", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND("INV-002", "Product with code {%s} not found", HttpStatus.NOT_FOUND),
    SUPPLIER_NOT_FOUND("INV-003", "Supplier with code {%s} not found", HttpStatus.NOT_FOUND),
    SHIPMENT_UNIT_NOT_FOUND("INV-004", "Shipment unit with id {%s} not found", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
