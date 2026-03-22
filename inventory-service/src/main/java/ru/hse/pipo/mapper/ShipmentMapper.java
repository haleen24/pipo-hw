package ru.hse.pipo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hse.inventory.model.CreateShipmentRequest;
import ru.hse.inventory.model.ShipmentResponse;
import ru.hse.pipo.entity.ShipmentEntity;
import ru.hse.pipo.model.Shipment;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ShipmentMapper {
    @Mapping(source = "supplierCode", target = "supplier.code")
    @Mapping(source = "productCode", target = "product.code")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Shipment toShipment(CreateShipmentRequest createShipmentRequest);

    Shipment toShipment(ShipmentEntity shipmentEntity);

    ShipmentEntity toShipmentEntity(Shipment shipment);

    @Mapping(target = "supplierCode", source = "supplier.code")
    @Mapping(target = "productCode", source = "product.code")
    ShipmentResponse toShipmentResponse(Shipment shipment);
}
