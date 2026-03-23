package ru.hse.pipo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.inventory.controller.SupplierApi;
import ru.hse.inventory.model.CreateSupplierRequest;
import ru.hse.inventory.model.PatchSupplierRequest;
import ru.hse.inventory.model.SupplierResponse;
import ru.hse.pipo.mapper.SupplierMapper;
import ru.hse.pipo.model.Supplier;
import ru.hse.pipo.service.SupplierService;

@RestController
@RequiredArgsConstructor
public class SupplierController implements SupplierApi {
    private final SupplierService supplierService;
    private final SupplierMapper supplierMapper;

    @Override
    public ResponseEntity<SupplierResponse> createSupplier(CreateSupplierRequest createSupplierRequest) {
        Supplier supplier = supplierMapper.toSupplier(createSupplierRequest);
        Supplier createdSupplier = supplierService.create(supplier);
        SupplierResponse supplierResponse = supplierMapper.toSupplierResponse(createdSupplier);
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierResponse);
    }

    @Override
    public ResponseEntity<SupplierResponse> getSupplier(String code) {
        Supplier supplier = supplierService.getByCode(code);
        return ResponseEntity.ok(supplierMapper.toSupplierResponse(supplier));
    }

    @Override
    public ResponseEntity<SupplierResponse> patchSupplier(String code, PatchSupplierRequest createSupplierRequest) {
        Supplier supplier = supplierMapper.toSupplier(createSupplierRequest);
        Supplier patchedSupplier = supplierService.patchByCode(code, supplier);
        return ResponseEntity.ok(supplierMapper.toSupplierResponse(patchedSupplier));
    }
}
