package ru.hse.pipo.service;

import ru.hse.pipo.model.Location;

public interface LocationService {
    Location getByCode(String code);
}
