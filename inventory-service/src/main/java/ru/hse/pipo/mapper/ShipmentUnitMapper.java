package ru.hse.pipo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hse.inventory.model.CreateShipmentUnitRequest;
import ru.hse.inventory.model.ShipmentUnitResponse;
import ru.hse.pipo.entity.ShipmentUnitEntity;
import ru.hse.pipo.model.ShipmentUnit;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ShipmentUnitMapper {
    @Mapping(source = "shipmentId", target = "shipment.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "location", ignore = true)
    ShipmentUnit toShipmentUnit(CreateShipmentUnitRequest createShipmentUnitRequest);

    ShipmentUnit toShipmentUnit(ShipmentUnitEntity shipmentUnitEntity);

    ShipmentUnitEntity toShipmentUnitEntity(ShipmentUnit shipmentUnit);

    @Mapping(source = "shipment.id", target = "shipmentId")
    @Mapping(source = "shipment.status", target = "shipmentStatus")
    @Mapping(source = "location.code", target = "locationCode")
    ShipmentUnitResponse toShipmentUnitResponse(ShipmentUnit shipment);
}
