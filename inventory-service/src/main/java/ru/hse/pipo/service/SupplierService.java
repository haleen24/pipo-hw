package ru.hse.pipo.service;

import ru.hse.pipo.model.Supplier;

public interface SupplierService {
    Supplier create(Supplier supplier);

    Supplier patchByCode(String code, Supplier supplier);

    Supplier getByCode(String code);

}
