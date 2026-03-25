package ru.hse.pipo.service;

import ru.hse.pipo.model.LocationType;

public interface LocationTypeService {
    LocationType create(LocationType locationType);

    LocationType getByCode(String code);

    void delete(String code);
}
