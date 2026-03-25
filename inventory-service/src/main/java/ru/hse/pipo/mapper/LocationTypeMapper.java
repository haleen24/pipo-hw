package ru.hse.pipo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hse.inventory.model.CreateLocationTypeRequest;
import ru.hse.inventory.model.LocationTypeResponse;
import ru.hse.pipo.entity.LocationTypeEntity;
import ru.hse.pipo.model.LocationType;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface LocationTypeMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    LocationType toLocationType(CreateLocationTypeRequest createLocationTypeRequest);

    LocationTypeResponse toLocationTypeResponse(LocationType locationType);

    LocationTypeEntity toEntity(LocationType locationType);

    LocationType toLocationType(LocationTypeEntity locationTypeEntity);
}
