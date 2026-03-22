package ru.hse.pipo.exception;

import lombok.Getter;

@Getter
public class InventoryException extends RuntimeException {
    private final InventoryExceptionCode code;

    public InventoryException(InventoryExceptionCode inventoryExceptionCode, String... args) {
        super(inventoryExceptionCode.getMessage().formatted((Object[]) args));
        code = inventoryExceptionCode;
    }

    public String getDetails() {
        return code.getCode() + " " + getMessage();
    }
}
