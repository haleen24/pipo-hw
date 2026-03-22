package ru.hse.pipo.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.pipo.entity.ShipmentUnitEntity;
import ru.hse.pipo.exception.InventoryException;
import ru.hse.pipo.exception.InventoryExceptionCode;
import ru.hse.pipo.mapper.ShipmentUnitMapper;
import ru.hse.pipo.model.ShipmentUnit;
import ru.hse.pipo.repository.ShipmentUnitRepository;

@Service
@RequiredArgsConstructor
public class ShipmentUnitServiceImpl implements ShipmentUnitService {
    private final ShipmentUnitRepository shipmentUnitRepository;
    private final ShipmentUnitMapper shipmentUnitMapper;

    @Override
    public ShipmentUnit create(ShipmentUnit shipmentUnit) {
        ShipmentUnitEntity shipmentUnitEntity = shipmentUnitMapper.toShipmentUnitEntity(shipmentUnit);
        ShipmentUnitEntity createdShipmentUnitEntity = shipmentUnitRepository.save(shipmentUnitEntity);
        return shipmentUnitMapper.toShipmentUnit(createdShipmentUnitEntity);
    }

    @Override
    public ShipmentUnit getById(Long id) {
        Optional<ShipmentUnitEntity> shipmentUnitEntity = shipmentUnitRepository.findById(id);
        if (shipmentUnitEntity.isEmpty()) {
            throw new InventoryException(InventoryExceptionCode.SHIPMENT_UNIT_NOT_FOUND, id.toString());
        }
        return shipmentUnitMapper.toShipmentUnit(shipmentUnitEntity.get());
    }
}
