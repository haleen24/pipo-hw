package ru.hse.pipo;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import ru.hse.inventory.model.CreateLocationRequest;
import ru.hse.inventory.model.LocationResponse;
import ru.hse.inventory.model.SupplierResponse;
import ru.hse.pipo.entity.LocationEntity;
import ru.hse.pipo.entity.LocationTypeEntity;
import ru.hse.pipo.entity.ZoneEntity;
import ru.hse.pipo.model.ZoneType;
import ru.hse.pipo.repository.LocationRepository;
import ru.hse.pipo.utils.DataGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.hse.pipo.exception.InventoryExceptionCode.LOCATION_CODE_IS_NOT_UNIQUE;
import static ru.hse.pipo.exception.InventoryExceptionCode.LOCATION_NOT_FOUND;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LocationTest extends CommonTestConfiguration {
    final String URL_BY_CODE = "/location/%s";
    final String URL = "/location";

    @Autowired
    DataGenerator dataGenerator;

    @Autowired
    LocationRepository locationRepository;

    @Test
    void locationCreateSuccessTest() {
        LocationTypeEntity locationTypeEntity = dataGenerator.generateLocationType();
        ZoneEntity zoneEntity = dataGenerator.generateZone(ZoneType.INBOUND);
        CreateLocationRequest createLocationRequest = new CreateLocationRequest();
        createLocationRequest.setCode(generateString());
        createLocationRequest.setLocationTypeCode(locationTypeEntity.getCode());
        createLocationRequest.setZoneCode(zoneEntity.getCode());

        ResponseEntity<LocationResponse> response =
            restClient.post().uri(URL).body(createLocationRequest).retrieve().toEntity(LocationResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        LocationResponse locationResponse = response.getBody();
        assertNotNull(locationResponse);
        assertNotNull(locationResponse.getId());
        assertEquals(createLocationRequest.getCode(), locationResponse.getCode());
        assertEquals(createLocationRequest.getLocationTypeCode(), locationResponse.getLocationTypeCode());
        assertEquals(createLocationRequest.getZoneCode(), locationResponse.getZoneCode());
    }

    @Test
    void locationGetSuccessTest() {
        LocationEntity locationEntity = dataGenerator.generateLocation(ZoneType.INBOUND);

        ResponseEntity<LocationResponse> response =
            restClient.get().uri(URL_BY_CODE.formatted(locationEntity.getCode())).retrieve().toEntity(LocationResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LocationResponse locationResponse = response.getBody();
        assertNotNull(locationResponse);
        assertNotNull(locationResponse.getId());
        assertNotNull(locationResponse.getCreatedAt());
        assertNotNull(locationResponse.getUpdatedAt());
        assertEquals(locationEntity.getCode(), locationResponse.getCode());
    }

    @Test
    void locationCreateErrorTestWhenCodeAlreadyExists() {
        LocationEntity locationEntity = dataGenerator.generateLocation(ZoneType.STORAGE);
        ZoneEntity zoneEntity = dataGenerator.generateZone(ZoneType.INBOUND);
        LocationTypeEntity locationTypeEntity = dataGenerator.generateLocationType();
        CreateLocationRequest createLocationRequest = new CreateLocationRequest();
        createLocationRequest.setCode(locationEntity.getCode());
        createLocationRequest.setZoneCode(zoneEntity.getCode());
        createLocationRequest.setLocationTypeCode(locationTypeEntity.getCode());

        HttpClientErrorException errorException = assertThrows(HttpClientErrorException.class,
            () -> restClient.post()
                .uri(URL)
                .body(createLocationRequest)
                .retrieve()
                .toEntity(SupplierResponse.class)
        );

        assertEquals(HttpStatus.CONFLICT, errorException.getStatusCode());
        assertTrue(errorException.getResponseBodyAsString().contains(LOCATION_CODE_IS_NOT_UNIQUE.getCode()));
    }

    @Test
    void locationGetErrorTestWhenLocationNotFound() {
        HttpClientErrorException errorException = assertThrows(HttpClientErrorException.class,
            () -> restClient.get()
                .uri(URL_BY_CODE.formatted(generateString()))
                .retrieve()
                .toEntity(LocationResponse.class)
        );

        assertEquals(HttpStatus.NOT_FOUND, errorException.getStatusCode());
        assertTrue(errorException.getResponseBodyAsString().contains(LOCATION_NOT_FOUND.getCode()));
    }

    @Test
    void locationDeleteSuccessTest() {
        LocationEntity locationEntity = dataGenerator.generateLocation(ZoneType.INBOUND);

        ResponseEntity<Void> response =
            restClient.delete().uri(URL_BY_CODE.formatted(locationEntity.getCode())).retrieve().toBodilessEntity();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(locationRepository.findByCode(locationEntity.getCode()));
    }

    @Test
    void locationDeleteErrorTestWhenLocationNotFound() {
        LocationEntity locationEntity = dataGenerator.generateLocation(ZoneType.INBOUND);

        HttpClientErrorException errorException = assertThrows(HttpClientErrorException.class,
            () -> restClient.delete()
                .uri(URL_BY_CODE.formatted(locationEntity.getLocationType().getCode()))
                .retrieve()
                .toEntity(LocationResponse.class)
        );

        assertEquals(HttpStatus.NOT_FOUND, errorException.getStatusCode());
        assertTrue(errorException.getResponseBodyAsString().contains(LOCATION_NOT_FOUND.getCode()));
    }
}