package ru.hse.pipo.service;

import ru.hse.pipo.model.Zone;

public interface ZoneService {
    Zone create(Zone zone);

    Zone getByCode(String code);

    void delete(String code);
}
