package ru.hse.pipo.service;

import java.util.Objects;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.hse.pipo.entity.LocationTypeEntity;
import ru.hse.pipo.exception.InventoryException;
import ru.hse.pipo.mapper.LocationTypeMapper;
import ru.hse.pipo.model.LocationType;
import ru.hse.pipo.repository.LocationTypeRepository;

import static ru.hse.pipo.exception.InventoryExceptionCode.CANT_CREATE_LOCATION_TYPE_WITHOUT_DIMENSIONS;
import static ru.hse.pipo.exception.InventoryExceptionCode.CANT_DELETE_LOCATION_TYPE_RELATED_TO_LOCATION;
import static ru.hse.pipo.exception.InventoryExceptionCode.LOCATION_TYPE_CODE_IS_NOT_UNIQUE;
import static ru.hse.pipo.exception.InventoryExceptionCode.LOCATION_TYPE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class LocationTypeServiceImpl implements LocationTypeService {
    private final LocationTypeMapper locationTypeMapper;
    private final LocationTypeRepository locationTypeRepository;

    @Override
    public LocationType create(LocationType locationType) {
        if (Stream.of(locationType.getLengthCm(), locationType.getWidthCm(), locationType.getHeightCm())
            .allMatch(Objects::isNull)) {
            if (locationType.getUnlimited() == null || !locationType.getUnlimited()) {
                throw new InventoryException(CANT_CREATE_LOCATION_TYPE_WITHOUT_DIMENSIONS);
            }
        }
        LocationTypeEntity locationTypeWithSameCode = locationTypeRepository.findByCode(locationType.getCode());
        if (locationTypeWithSameCode != null) {
            throw new InventoryException(LOCATION_TYPE_CODE_IS_NOT_UNIQUE, locationType.getCode());
        }
        LocationTypeEntity locationTypeEntity = locationTypeMapper.toEntity(locationType);
        LocationTypeEntity createdLocationTypeEntity = locationTypeRepository.save(locationTypeEntity);
        return locationTypeMapper.toLocationType(createdLocationTypeEntity);
    }

    @Override
    public LocationType getByCode(String code) {
        LocationTypeEntity locationTypeEntity = locationTypeRepository.findByCode(code);
        if (locationTypeEntity == null) {
            throw new InventoryException(LOCATION_TYPE_NOT_FOUND, code);
        }
        return locationTypeMapper.toLocationType(locationTypeEntity);
    }

    @Override
    public void delete(String code) {
        LocationType locationType = getByCode(code);
        LocationTypeEntity locationTypeEntity = locationTypeMapper.toEntity(locationType);
        try {
            locationTypeRepository.delete(locationTypeEntity);
        } catch (DataAccessException e) {
            throw new InventoryException(CANT_DELETE_LOCATION_TYPE_RELATED_TO_LOCATION, code);
        }
    }
}
