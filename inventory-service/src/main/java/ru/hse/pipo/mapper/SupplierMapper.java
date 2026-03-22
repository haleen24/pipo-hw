package ru.hse.pipo.mapper;

import org.mapstruct.Mapper;
import ru.hse.pipo.entity.SupplierEntity;
import ru.hse.pipo.model.Supplier;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface SupplierMapper {
    Supplier toSupplier(SupplierEntity supplierEntity);
}
