package ru.hse.pipo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.inventory.controller.LocationApi;
import ru.hse.inventory.model.CreateLocationRequest;
import ru.hse.inventory.model.CreateLocationTypeRequest;
import ru.hse.inventory.model.LocationResponse;
import ru.hse.inventory.model.LocationTypeResponse;
import ru.hse.pipo.mapper.LocationMapper;
import ru.hse.pipo.mapper.LocationTypeMapper;
import ru.hse.pipo.model.Location;
import ru.hse.pipo.model.LocationType;
import ru.hse.pipo.service.LocationService;
import ru.hse.pipo.service.LocationTypeService;

@RestController
@RequiredArgsConstructor
public class LocationController implements LocationApi {
    private final LocationMapper locationMapper;
    private final LocationTypeMapper locationTypeMapper;
    private final LocationService locationService;
    private final LocationTypeService locationTypeService;

    @Override
    public ResponseEntity<LocationResponse> createLocation(CreateLocationRequest createLocationRequest) {
        Location location = locationMapper.toLocation(createLocationRequest);
        Location createdLocation = locationService.create(location);
        LocationResponse locationResponse = locationMapper.toLocationResponse(createdLocation);
        return ResponseEntity.status(HttpStatus.CREATED).body(locationResponse);
    }

    @Override
    public ResponseEntity<LocationTypeResponse> createLocationType(CreateLocationTypeRequest createLocationTypeRequest) {
        LocationType locationType = locationTypeMapper.toLocationType(createLocationTypeRequest);
        LocationType createdLocationType = locationTypeService.create(locationType);
        LocationTypeResponse locationTypeResponse = locationTypeMapper.toLocationTypeResponse(createdLocationType);
        return ResponseEntity.status(HttpStatus.CREATED).body(locationTypeResponse);
    }

    @Override
    public ResponseEntity<Void> deleteLocation(String code) {
        locationService.deleteByCode(code);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteLocationType(String code) {
        locationTypeService.delete(code);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<LocationResponse> getLocation(String code) {
        Location location = locationService.getByCode(code);
        LocationResponse locationResponse = locationMapper.toLocationResponse(location);
        return ResponseEntity.ok(locationResponse);
    }

    @Override
    public ResponseEntity<LocationTypeResponse> getLocationType(String code) {
        LocationType locationType = locationTypeService.getByCode(code);
        LocationTypeResponse locationTypeResponse = locationTypeMapper.toLocationTypeResponse(locationType);
        return ResponseEntity.ok(locationTypeResponse);
    }
}
