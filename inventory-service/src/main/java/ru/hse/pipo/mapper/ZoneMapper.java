package ru.hse.pipo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hse.inventory.model.CreateZoneRequest;
import ru.hse.inventory.model.ZoneResponse;
import ru.hse.pipo.entity.ZoneEntity;
import ru.hse.pipo.model.Zone;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ZoneMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Zone toZone(CreateZoneRequest createZoneRequest);

    Zone toZone(ZoneEntity zoneEntity);

    ZoneResponse toZoneResponse(Zone zone);

    ZoneEntity toZoneEntity(Zone zone);

}
