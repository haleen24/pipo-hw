package ru.hse.pipo.service;

import ru.hse.pipo.model.Location;
import ru.hse.pipo.model.LocationType;

public interface LocationService {
    Location create(Location location);

    Location getByCode(String code);

    void deleteByCode(String code);
}
