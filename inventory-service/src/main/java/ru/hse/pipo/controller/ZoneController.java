package ru.hse.pipo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.inventory.controller.ZoneApi;
import ru.hse.inventory.model.CreateZoneRequest;
import ru.hse.inventory.model.ZoneResponse;
import ru.hse.pipo.mapper.ZoneMapper;
import ru.hse.pipo.model.Zone;
import ru.hse.pipo.service.ZoneService;

@RestController
@RequiredArgsConstructor
public class ZoneController implements ZoneApi {
    private final ZoneService zoneService;
    private final ZoneMapper zoneMapper;

    @Override
    public ResponseEntity<ZoneResponse> createZone(CreateZoneRequest createZoneRequest) {
        Zone zone = zoneMapper.toZone(createZoneRequest);
        Zone createdZone = zoneService.create(zone);
        ZoneResponse zoneResponse = zoneMapper.toZoneResponse(createdZone);
        return ResponseEntity.status(HttpStatus.CREATED).body(zoneResponse);
    }

    @Override
    public ResponseEntity<Void> deleteZone(String code) {
        zoneService.delete(code);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ZoneResponse> getZone(String code) {
        Zone zone = zoneService.getByCode(code);
        return ResponseEntity.ofNullable(zoneMapper.toZoneResponse(zone));
    }
}
