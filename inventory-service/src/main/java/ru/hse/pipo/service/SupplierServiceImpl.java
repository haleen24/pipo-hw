package ru.hse.pipo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.pipo.entity.SupplierEntity;
import ru.hse.pipo.exception.InventoryException;
import ru.hse.pipo.mapper.SupplierMapper;
import ru.hse.pipo.model.Supplier;
import ru.hse.pipo.repository.SupplierRepository;

import static ru.hse.pipo.exception.InventoryExceptionCode.SUPPLIER_CODE_NOT_UNIQUE;
import static ru.hse.pipo.exception.InventoryExceptionCode.SUPPLIER_NOT_FOUND;
import static ru.hse.pipo.utils.PatchUtils.setIfNotNull;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Override
    public Supplier create(Supplier supplier) {
        validateCode(supplier.getCode());
        SupplierEntity supplierEntity = supplierMapper.toSupplierEntity(supplier);
        supplierRepository.save(supplierEntity);
        return supplierMapper.toSupplier(supplierEntity);
    }

    @Override
    public Supplier patchByCode(String code, Supplier supplier) {
        Supplier existingSupplier = getByCode(code);
        validateCode(supplier.getCode());
        setIfNotNull(supplier.getCode(), existingSupplier::setCode);
        setIfNotNull(supplier.getName(), existingSupplier::setName);
        SupplierEntity supplierEntity = supplierMapper.toSupplierEntity(existingSupplier);
        supplierRepository.save(supplierEntity);
        return supplierMapper.toSupplier(supplierEntity);
    }

    @Override
    public Supplier getByCode(String code) {
        SupplierEntity supplierEntity = findSupplierEntityByCode(code);
        if (supplierEntity == null) {
            throw new InventoryException(SUPPLIER_NOT_FOUND, code);
        }
        return supplierMapper.toSupplier(supplierEntity);
    }

    private void validateCode(String code) {
        if (code == null) {
            return;
        }
        SupplierEntity supplierEntity = findSupplierEntityByCode(code);
        if (supplierEntity != null) {
            throw new InventoryException(SUPPLIER_CODE_NOT_UNIQUE);
        }
    }

    private SupplierEntity findSupplierEntityByCode(String code) {
        return supplierRepository.findByCode(code);
    }
}
