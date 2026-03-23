package ru.hse.pipo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.hse.pipo.entity.ZoneEntity;
import ru.hse.pipo.exception.InventoryException;
import ru.hse.pipo.mapper.ZoneMapper;
import ru.hse.pipo.model.Zone;
import ru.hse.pipo.repository.ZoneRepository;

import static ru.hse.pipo.exception.InventoryExceptionCode.CANT_DELETE_ZONE_RELATED_TO_LOCATION;
import static ru.hse.pipo.exception.InventoryExceptionCode.ZONE_CODE_NOT_UNIQUE;
import static ru.hse.pipo.exception.InventoryExceptionCode.ZONE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ZoneServiceImpl implements ZoneService {
    private final ZoneRepository zoneRepository;
    private final ZoneMapper zoneMapper;

    @Override
    public Zone create(Zone zone) {
        validateCode(zone.getCode());
        ZoneEntity zoneEntity = zoneMapper.toZoneEntity(zone);
        ZoneEntity createdZoneEntity = zoneRepository.save(zoneEntity);
        return zoneMapper.toZone(createdZoneEntity);
    }

    @Override
    public Zone getByCode(String code) {
        ZoneEntity zoneEntity = zoneRepository.findByCode(code);
        if (zoneEntity == null) {
            throw new InventoryException(ZONE_NOT_FOUND, code);
        }
        return zoneMapper.toZone(zoneEntity);
    }

    @Override
    public void delete(String code) {
        Zone zone = getByCode(code);
        ZoneEntity zoneEntity = zoneMapper.toZoneEntity(zone);
        try {
            zoneRepository.delete(zoneEntity);
        } catch (DataAccessException dataAccessException) {
            throw new InventoryException(CANT_DELETE_ZONE_RELATED_TO_LOCATION, code);
        }
    }

    private void validateCode(String code) {
        ZoneEntity zoneEntity = zoneRepository.findByCode(code);
        if (zoneEntity != null) {
            throw new InventoryException(ZONE_CODE_NOT_UNIQUE);
        }
    }
}
