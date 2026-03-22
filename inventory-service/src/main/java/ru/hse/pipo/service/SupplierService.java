package ru.hse.pipo.service;

import ru.hse.pipo.model.Supplier;

public interface SupplierService {
    Supplier getByCode(String code);
}
