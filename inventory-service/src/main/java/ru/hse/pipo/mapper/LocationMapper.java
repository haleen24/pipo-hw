package ru.hse.pipo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hse.inventory.model.CreateLocationRequest;
import ru.hse.inventory.model.LocationResponse;
import ru.hse.pipo.entity.LocationEntity;
import ru.hse.pipo.model.Location;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface LocationMapper {
    Location toLocation(LocationEntity locationEntity);

    @Mapping(target = "locationTypeCode", source = "locationType.code")
    @Mapping(target = "zoneCode", source = "zone.code")
    LocationResponse toLocationResponse(Location location);

    @Mapping(target = "locationType.code", source = "locationTypeCode")
    @Mapping(target = "zone.code", source = "zoneCode")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Location toLocation(CreateLocationRequest createLocationRequest);

    LocationEntity toLocationEntity(Location location);
}
