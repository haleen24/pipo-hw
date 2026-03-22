package ru.hse.pipo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.pipo.entity.SupplierEntity;
import ru.hse.pipo.exception.InventoryException;
import ru.hse.pipo.mapper.SupplierMapper;
import ru.hse.pipo.model.Supplier;
import ru.hse.pipo.repository.SupplierRepository;

import static ru.hse.pipo.exception.InventoryExceptionCode.SUPPLIER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Override
    public Supplier getByCode(String code) {
        SupplierEntity supplierEntity = supplierRepository.findByCode(code);
        if (supplierEntity == null) {
            throw new InventoryException(SUPPLIER_NOT_FOUND, code);
        }
        return supplierMapper.toSupplier(supplierEntity);
    }
}
