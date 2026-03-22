package ru.hse.pipo.service;

import ru.hse.pipo.model.ShipmentUnit;

public interface ShipmentUnitService {
    ShipmentUnit create(ShipmentUnit shipmentUnit);

    ShipmentUnit getById(Long id);
}
