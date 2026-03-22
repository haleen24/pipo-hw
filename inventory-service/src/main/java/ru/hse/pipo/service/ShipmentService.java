package ru.hse.pipo.service;

import ru.hse.pipo.model.Shipment;
import ru.hse.pipo.model.ShipmentUnit;

public interface ShipmentService {
    Shipment create(Shipment shipment);

    Shipment getById(Long id);

    ShipmentUnit createShipmentUnit(ShipmentUnit shipmentUnit);

    ShipmentUnit getShipmentUnitById(Long id);

    ShipmentUnit moveShipmentUnit(Long shipmentUnitId, String locationCode);
}
