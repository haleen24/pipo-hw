package ru.hse.pipo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hse.inventory.model.CreateSupplierRequest;
import ru.hse.inventory.model.PatchSupplierRequest;
import ru.hse.inventory.model.SupplierResponse;
import ru.hse.pipo.entity.SupplierEntity;
import ru.hse.pipo.model.Supplier;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface SupplierMapper {
    Supplier toSupplier(SupplierEntity supplierEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Supplier toSupplier(CreateSupplierRequest createSupplierRequest);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Supplier toSupplier(PatchSupplierRequest patchSupplierRequest);

    SupplierResponse toSupplierResponse(Supplier supplier);

    SupplierEntity toSupplierEntity(Supplier supplier);
}
