package ru.hse.pipo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.pipo.entity.LocationEntity;
import ru.hse.pipo.exception.InventoryException;
import ru.hse.pipo.mapper.LocationMapper;
import ru.hse.pipo.model.Location;
import ru.hse.pipo.model.LocationType;
import ru.hse.pipo.model.Zone;
import ru.hse.pipo.repository.LocationRepository;

import static ru.hse.pipo.exception.InventoryExceptionCode.LOCATION_CODE_IS_NOT_UNIQUE;
import static ru.hse.pipo.exception.InventoryExceptionCode.LOCATION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final LocationTypeService locationTypeService;
    private final ZoneService zoneService;

    @Override
    public Location create(Location location) {
        if (locationRepository.findByCode(location.getCode()) != null) {
            throw new InventoryException(LOCATION_CODE_IS_NOT_UNIQUE, location.getCode());
        }
        LocationType locationType = locationTypeService.getByCode(location.getLocationType().getCode());
        location.setLocationType(locationType);
        Zone zone = zoneService.getByCode(location.getZone().getCode());
        location.setZone(zone);
        LocationEntity locationEntity = locationMapper.toLocationEntity(location);
        LocationEntity createdLocationEntity = locationRepository.save(locationEntity);
        return locationMapper.toLocation(createdLocationEntity);
    }

    @Override
    public Location getByCode(String code) {
        LocationEntity locationEntity = locationRepository.findByCode(code);
        if (locationEntity == null) {
            throw new InventoryException(LOCATION_NOT_FOUND, code);
        }
        return locationMapper.toLocation(locationEntity);
    }

    @Override
    public void deleteByCode(String code) {
        Location location = getByCode(code);
        LocationEntity locationEntity = locationMapper.toLocationEntity(location);
        locationRepository.delete(locationEntity);
    }
}
