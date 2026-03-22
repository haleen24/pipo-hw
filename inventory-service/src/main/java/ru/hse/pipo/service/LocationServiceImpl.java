package ru.hse.pipo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.pipo.entity.LocationEntity;
import ru.hse.pipo.exception.InventoryException;
import ru.hse.pipo.exception.InventoryExceptionCode;
import ru.hse.pipo.mapper.LocationMapper;
import ru.hse.pipo.model.Location;
import ru.hse.pipo.repository.LocationRepository;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    public Location getByCode(String code) {
        LocationEntity locationEntity = locationRepository.findByCode(code);
        if (locationEntity == null) {
            throw new InventoryException(InventoryExceptionCode.LOCATION_NOT_FOUND, code);
        }
        return locationMapper.toLocation(locationEntity);
    }
}
