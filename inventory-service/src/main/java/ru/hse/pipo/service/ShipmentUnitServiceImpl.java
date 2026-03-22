package ru.hse.pipo.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.pipo.entity.ShipmentUnitEntity;
import ru.hse.pipo.exception.InventoryException;
import ru.hse.pipo.mapper.ShipmentUnitMapper;
import ru.hse.pipo.model.Location;
import ru.hse.pipo.model.LocationType;
import ru.hse.pipo.model.Shipment;
import ru.hse.pipo.model.ShipmentUnit;
import ru.hse.pipo.model.ZoneType;
import ru.hse.pipo.repository.ShipmentUnitRepository;

import static ru.hse.pipo.exception.InventoryExceptionCode.INBOUND_SHIPMENT_PLACED_NOT_IN_INBOUND;
import static ru.hse.pipo.exception.InventoryExceptionCode.NO_ROOM_FOR_SHIPMENT_UNIT_IN_LOCATION;
import static ru.hse.pipo.exception.InventoryExceptionCode.SHIPMENT_UNIT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ShipmentUnitServiceImpl implements ShipmentUnitService {
    private final ShipmentUnitRepository shipmentUnitRepository;
    private final ShipmentUnitMapper shipmentUnitMapper;
    private final LocationService locationService;

    @Override
    public ShipmentUnit create(ShipmentUnit shipmentUnit) {
        ShipmentUnitEntity shipmentUnitEntity = shipmentUnitMapper.toShipmentUnitEntity(shipmentUnit);
        ShipmentUnitEntity createdShipmentUnitEntity = shipmentUnitRepository.save(shipmentUnitEntity);
        return shipmentUnitMapper.toShipmentUnit(createdShipmentUnitEntity);
    }

    @Override
    public ShipmentUnit getById(Long id) {
        ShipmentUnitEntity shipmentUnitEntity = getShipmentUnitEntity(id);
        return shipmentUnitMapper.toShipmentUnit(shipmentUnitEntity);
    }

    @Override
    public ShipmentUnit moveById(Long id, String locationCode) {
        ShipmentUnit shipmentUnit = getById(id);
        Location location = locationService.getByCode(locationCode);
        if (location.getZone().getType() != ZoneType.INBOUND) {
            throw new InventoryException(INBOUND_SHIPMENT_PLACED_NOT_IN_INBOUND);
        }
        validateSize(shipmentUnit, location);
        shipmentUnit.setLocation(location);
        return save(shipmentUnit);
    }

    @Override
    public Boolean isShipmentComplete(Shipment shipment) {
        List<ShipmentUnitEntity> shipmentUnitEntities = shipmentUnitRepository.findAllByShipmentId(shipment.getId());
        return (shipmentUnitEntities.size() == shipment.getShipmentUnitCount()) && shipmentUnitEntities.stream()
            .map(ShipmentUnitEntity::getLocation)
            .allMatch(Objects::nonNull);
    }

    private ShipmentUnitEntity getShipmentUnitEntity(Long id) {
        Optional<ShipmentUnitEntity> shipmentUnitEntity = shipmentUnitRepository.findById(id);
        if (shipmentUnitEntity.isEmpty()) {
            throw new InventoryException(SHIPMENT_UNIT_NOT_FOUND, id.toString());
        }
        return shipmentUnitEntity.get();
    }

    private void validateSize(ShipmentUnit shipmentUnit, Location location) {
        List<Long> shipmentUnitDimensions = Stream.of(shipmentUnit.getHeightCm(), shipmentUnit.getWidthCm(), shipmentUnit.getLengthCm())
            .sorted()
            .toList();
        LocationType locationType = location.getLocationType();
        List<Long> locationDimensions = Stream.of(locationType.getHeightCm(), locationType.getWidthCm(), locationType.getLengthCm())
            .sorted()
            .toList();
        for (int i = 0; i < shipmentUnitDimensions.size(); ++i) {
            if (shipmentUnitDimensions.get(i) > locationDimensions.get(i)) {
                throw new InventoryException(NO_ROOM_FOR_SHIPMENT_UNIT_IN_LOCATION, shipmentUnit.getId().toString(), location.getCode());
            }
        }
        List<ShipmentUnitEntity> currentShipmentUnitEntitiesInLocation = shipmentUnitRepository.findAllByLocationId(location.getId());
        long currentVolume = currentShipmentUnitEntitiesInLocation.stream()
            .map(this::getVolume)
            .reduce(0L, Long::sum);
        long shipmentUnitVolume = shipmentUnit.getHeightCm() * shipmentUnit.getWidthCm() * shipmentUnit.getLengthCm();
        long locationVolume = locationType.getHeightCm() * locationType.getWidthCm() * locationType.getLengthCm();
        if (currentVolume + shipmentUnitVolume > locationVolume) {
            throw new InventoryException(NO_ROOM_FOR_SHIPMENT_UNIT_IN_LOCATION, shipmentUnit.getId().toString(), location.getCode());
        }
    }

    private Long getVolume(ShipmentUnitEntity shipmentUnitEntity) {
        return shipmentUnitEntity.getHeightCm() * shipmentUnitEntity.getWidthCm() * shipmentUnitEntity.getLengthCm();
    }

    private ShipmentUnit save(ShipmentUnit shipmentUnit) {
        ShipmentUnitEntity shipmentUnitEntity = shipmentUnitMapper.toShipmentUnitEntity(shipmentUnit);
        ShipmentUnitEntity savedShipmentUnitEntity = shipmentUnitRepository.save(shipmentUnitEntity);
        return shipmentUnitMapper.toShipmentUnit(savedShipmentUnitEntity);
    }
}
