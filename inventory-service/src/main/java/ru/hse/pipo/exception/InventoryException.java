package ru.hse.pipo.exception;

public class InventoryException extends RuntimeException {
    public InventoryException(InventoryExceptionCode inventoryExceptionCode, String... args) {
        super(inventoryExceptionCode.getMessage().formatted((Object) args));
    }
}
