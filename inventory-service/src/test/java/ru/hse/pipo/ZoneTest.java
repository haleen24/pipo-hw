package ru.hse.pipo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import ru.hse.inventory.model.CreateZoneRequest;
import ru.hse.inventory.model.SupplierResponse;
import ru.hse.inventory.model.ZoneResponse;
import ru.hse.pipo.entity.LocationEntity;
import ru.hse.pipo.entity.ZoneEntity;
import ru.hse.pipo.model.ZoneType;
import ru.hse.pipo.repository.ZoneRepository;
import ru.hse.pipo.utils.DataGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.hse.pipo.exception.InventoryExceptionCode.CANT_DELETE_ZONE_RELATED_TO_LOCATION;
import static ru.hse.pipo.exception.InventoryExceptionCode.ZONE_CODE_NOT_UNIQUE;
import static ru.hse.pipo.exception.InventoryExceptionCode.ZONE_NOT_FOUND;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ZoneTest extends CommonTestConfiguration {
    final String URL_BY_CODE = "/zone/%s";
    final String URL = "/zone";

    @Autowired
    DataGenerator dataGenerator;

    @Autowired
    ZoneRepository zoneRepository;

    @Test
    void zoneCreateSuccessTest() {
        CreateZoneRequest createZoneRequest = new CreateZoneRequest();
        createZoneRequest.setCode(generateString());
        createZoneRequest.setType(ZoneType.INBOUND.name());

        ResponseEntity<ZoneResponse> response =
            restClient.post().uri(URL).body(createZoneRequest).retrieve().toEntity(ZoneResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ZoneResponse zoneResponse = response.getBody();
        assertNotNull(zoneResponse);
        assertNotNull(zoneResponse.getId());
        assertEquals(createZoneRequest.getCode(), zoneResponse.getCode());
        assertEquals(createZoneRequest.getType(), zoneResponse.getType());
    }

    @Test
    void zoneGetSuccessTest() {
        ZoneEntity zoneEntity = dataGenerator.generateZone(ZoneType.STORAGE);

        ResponseEntity<ZoneResponse> response =
            restClient.get().uri(URL_BY_CODE.formatted(zoneEntity.getCode())).retrieve().toEntity(ZoneResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ZoneResponse zoneResponse = response.getBody();
        assertNotNull(zoneResponse);
        assertNotNull(zoneResponse.getId());
        assertNotNull(zoneResponse.getCreatedAt());
        assertNotNull(zoneResponse.getUpdatedAt());
        assertEquals(zoneEntity.getCode(), zoneResponse.getCode());
        assertEquals(zoneEntity.getType(), zoneResponse.getType());
    }

    @Test
    void zoneCreateErrorTestWhenCodeAlreadyExists() {
        ZoneEntity zoneEntity = dataGenerator.generateZone(ZoneType.INBOUND);
        CreateZoneRequest createZoneRequest = new CreateZoneRequest();
        createZoneRequest.setCode(zoneEntity.getCode());
        createZoneRequest.setType(ZoneType.INBOUND.name());

        HttpClientErrorException errorException = assertThrows(HttpClientErrorException.class,
            () -> restClient.post()
                .uri(URL)
                .body(createZoneRequest)
                .retrieve()
                .toEntity(SupplierResponse.class)
        );

        assertEquals(HttpStatus.CONFLICT, errorException.getStatusCode());
        assertTrue(errorException.getResponseBodyAsString().contains(ZONE_CODE_NOT_UNIQUE.getCode()));
    }

    @Test
    void zoneGetErrorTestWhenSupplierNotExist() {
        HttpClientErrorException errorException = assertThrows(HttpClientErrorException.class,
            () -> restClient.get()
                .uri(URL_BY_CODE.formatted(generateString()))
                .retrieve()
                .toEntity(ZoneResponse.class)
        );

        assertEquals(HttpStatus.NOT_FOUND, errorException.getStatusCode());
        assertTrue(errorException.getResponseBodyAsString().contains(ZONE_NOT_FOUND.getCode()));
    }

    @Test
    void zoneDeleteSuccessTest() {
        ZoneEntity zoneEntity = dataGenerator.generateZone(ZoneType.STORAGE);

        ResponseEntity<Void> response = restClient.delete().uri(URL_BY_CODE.formatted(zoneEntity.getCode())).retrieve().toBodilessEntity();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(zoneRepository.findByCode(zoneEntity.getCode()));
    }

    @Test
    void zoneDeleteErrorTestWhenZoneNotFound() {
        HttpClientErrorException errorException = assertThrows(HttpClientErrorException.class,
            () -> restClient.delete()
                .uri(URL_BY_CODE.formatted(generateString()))
                .retrieve()
                .toEntity(ZoneResponse.class)
        );

        assertEquals(HttpStatus.NOT_FOUND, errorException.getStatusCode());
        assertTrue(errorException.getResponseBodyAsString().contains(ZONE_NOT_FOUND.getCode()));
    }

    @Test
    void zoneDeleteErrorTestWhenLocationInZoneExists() {
        LocationEntity locationEntity = dataGenerator.generateLocation(ZoneType.STORAGE);
        ZoneEntity zoneEntity = locationEntity.getZone();

        HttpClientErrorException errorException = assertThrows(HttpClientErrorException.class,
            () -> restClient.delete()
                .uri(URL_BY_CODE.formatted(zoneEntity.getCode()))
                .retrieve()
                .toEntity(ZoneResponse.class)
        );

        assertEquals(HttpStatus.FORBIDDEN, errorException.getStatusCode());
        assertTrue(errorException.getResponseBodyAsString().contains(CANT_DELETE_ZONE_RELATED_TO_LOCATION.getCode()));
    }
}
