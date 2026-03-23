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
    SHIPMENT_UNIT_NOT_FOUND("INV-004", "Shipment unit with id {%s} not found", HttpStatus.NOT_FOUND),
    LOCATION_NOT_FOUND("INV-005", "Location with code {%s} not found", HttpStatus.NOT_FOUND),
    INBOUND_SHIPMENT_PLACED_NOT_IN_INBOUND("INV-006", "Can't place inbound shipment in not inbound location", HttpStatus.FORBIDDEN),
    NO_ROOM_FOR_SHIPMENT_UNIT_IN_LOCATION("INV-007", "No room for shipment unit with id {%s} in location with code {%}",
        HttpStatus.CONFLICT),
    SUPPLIER_CODE_NOT_UNIQUE("INV-008", "Supplier code is not unique", HttpStatus.CONFLICT),
    PRODUCT_CODE_NOT_UNIQUE("INV-009", "Product code is not unique", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
