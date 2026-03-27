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
    PRODUCT_CODE_NOT_UNIQUE("INV-009", "Product code is not unique", HttpStatus.CONFLICT),
    ZONE_CODE_NOT_UNIQUE("INV-010", "Zone code is not unique", HttpStatus.CONFLICT),
    ZONE_NOT_FOUND("INV-011", "Zone with code {%s} not found", HttpStatus.NOT_FOUND),
    CANT_DELETE_ZONE_RELATED_TO_LOCATION("INV-012", "Can't delete zone with code {%s}, related to locations", HttpStatus.FORBIDDEN),
    CANT_DELETE_LOCATION_TYPE_RELATED_TO_LOCATION("INV-013", "Can't delete location type with code {%s}, related to location",
        HttpStatus.FORBIDDEN),
    LOCATION_TYPE_NOT_FOUND("INV-014", "Location type with code {%s} not found", HttpStatus.NOT_FOUND),
    CANT_CREATE_LOCATION_TYPE_WITHOUT_DIMENSIONS("INV-015", "Can't create location type without dimensions", HttpStatus.BAD_REQUEST),
    LOCATION_TYPE_CODE_IS_NOT_UNIQUE("INV-016", "Location type with code {%s} already exists", HttpStatus.CONFLICT),
    LOCATION_CODE_IS_NOT_UNIQUE("INV-017", "Location with code {%s} already exists", HttpStatus.CONFLICT),
    RECEIVER_CODE_IS_NOT_UNIQUE("INV-018", "Receiver with code {%s} already exists", HttpStatus.CONFLICT),
    RECEIVER_NOT_FOUND("INV-019", "Receiver with code {%s} not found", HttpStatus.NOT_FOUND),
    OUTBOUND_SHIPMENT_NOT_FOUND("INV-020", "Outbound shipment with id {%s} not found", HttpStatus.NOT_FOUND),
    NOT_ENOUGH_STOCK("INV-021", "Not enough stock for product with code {%s}. Required: {%s}, available: {%s}", HttpStatus.BAD_REQUEST),
    STOCK_NOT_FOUND("INV-022", "Product with code {%s} not found in location with code {%s}", HttpStatus.NOT_FOUND),
    OUTBOUND_SHIPMENT_CANCELED("INV-023", "Outbound shipment with id {%s} is canceled", HttpStatus.BAD_REQUEST),
    WITHDRAWAL_NOT_FOUND("INV-024", "Withdrawal with id {%s} not found",HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
