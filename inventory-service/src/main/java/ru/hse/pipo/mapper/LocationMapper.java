package ru.hse.pipo.mapper;

import org.mapstruct.Mapper;
import ru.hse.pipo.entity.LocationEntity;
import ru.hse.pipo.model.Location;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface LocationMapper {
    Location toLocation(LocationEntity locationEntity);
}
