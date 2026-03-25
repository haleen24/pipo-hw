package ru.hse.pipo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import ru.hse.inventory.model.CreateLocationTypeRequest;
import ru.hse.inventory.model.LocationTypeResponse;
import ru.hse.inventory.model.SupplierResponse;
import ru.hse.pipo.entity.LocationEntity;
import ru.hse.pipo.entity.LocationTypeEntity;
import ru.hse.pipo.model.ZoneType;
import ru.hse.pipo.repository.LocationTypeRepository;
import ru.hse.pipo.utils.DataGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.hse.pipo.exception.InventoryExceptionCode.CANT_CREATE_LOCATION_TYPE_WITHOUT_DIMENSIONS;
import static ru.hse.pipo.exception.InventoryExceptionCode.CANT_DELETE_LOCATION_TYPE_RELATED_TO_LOCATION;
import static ru.hse.pipo.exception.InventoryExceptionCode.LOCATION_TYPE_CODE_IS_NOT_UNIQUE;
import static ru.hse.pipo.exception.InventoryExceptionCode.LOCATION_TYPE_NOT_FOUND;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LocationTypeTest extends CommonTestConfiguration {
    final String URL_BY_CODE = "/locationType/%s";
    final String URL = "/locationType";

    @Autowired
    DataGenerator dataGenerator;

    @Autowired
    LocationTypeRepository locationTypeRepository;

    @Test
    void locationTypeCreateSuccessTestWithAllDimensions() {
        CreateLocationTypeRequest createLocationTypeRequest = new CreateLocationTypeRequest();
        createLocationTypeRequest.setCode(generateString());
        createLocationTypeRequest.setHeightCm(generateLong());
        createLocationTypeRequest.setWidthCm(generateLong());
        createLocationTypeRequest.setLengthCm(generateLong());

        ResponseEntity<LocationTypeResponse> response =
            restClient.post().uri(URL).body(createLocationTypeRequest).retrieve().toEntity(LocationTypeResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        LocationTypeResponse locationTypeResponse = response.getBody();
        assertNotNull(locationTypeResponse);
        assertNotNull(locationTypeResponse.getId());
        assertEquals(createLocationTypeRequest.getCode(), locationTypeResponse.getCode());
        assertEquals(createLocationTypeRequest.getHeightCm(), locationTypeResponse.getHeightCm());
        assertEquals(createLocationTypeRequest.getWidthCm(), locationTypeResponse.getWidthCm());
        assertEquals(createLocationTypeRequest.getLengthCm(), locationTypeResponse.getLengthCm());
        assertFalse(locationTypeResponse.getUnlimited());
    }

    @Test
    void locationTypeCreateSuccessTestUnlimited() {
        CreateLocationTypeRequest createLocationTypeRequest = new CreateLocationTypeRequest();
        createLocationTypeRequest.setCode(generateString());
        createLocationTypeRequest.setUnlimited(true);

        ResponseEntity<LocationTypeResponse> response =
            restClient.post().uri(URL).body(createLocationTypeRequest).retrieve().toEntity(LocationTypeResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        LocationTypeResponse locationTypeResponse = response.getBody();
        assertNotNull(locationTypeResponse);
        assertNotNull(locationTypeResponse.getId());
        assertEquals(createLocationTypeRequest.getCode(), locationTypeResponse.getCode());
        assertTrue(locationTypeResponse.getUnlimited());
        assertNull(locationTypeResponse.getHeightCm());
        assertNull(locationTypeResponse.getWidthCm());
        assertNull(locationTypeResponse.getLengthCm());
    }

    @Test
    void locationTypeGetSuccessTest() {
        LocationTypeEntity locationTypeEntity = dataGenerator.generateLocationType();

        ResponseEntity<LocationTypeResponse> response =
            restClient.get().uri(URL_BY_CODE.formatted(locationTypeEntity.getCode())).retrieve().toEntity(LocationTypeResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LocationTypeResponse locationTypeResponse = response.getBody();
        assertNotNull(locationTypeResponse);
        assertNotNull(locationTypeResponse.getId());
        assertNotNull(locationTypeResponse.getCreatedAt());
        assertNotNull(locationTypeResponse.getUpdatedAt());
        assertEquals(locationTypeEntity.getCode(), locationTypeResponse.getCode());
        assertEquals(locationTypeEntity.getLengthCm(), locationTypeResponse.getLengthCm());
        assertEquals(locationTypeEntity.getWidthCm(), locationTypeResponse.getWidthCm());
        assertEquals(locationTypeEntity.getHeightCm(), locationTypeResponse.getHeightCm());
        assertEquals(locationTypeEntity.getUnlimited(), locationTypeResponse.getUnlimited());
    }

    @Test
    void locationTypeCreateErrorTestWhenCodeAlreadyExists() {
        LocationTypeEntity locationTypeEntity = dataGenerator.generateLocationType();
        CreateLocationTypeRequest createLocationTypeRequest = new CreateLocationTypeRequest();
        createLocationTypeRequest.setCode(locationTypeEntity.getCode());
        createLocationTypeRequest.setUnlimited(true);

        HttpClientErrorException errorException = assertThrows(HttpClientErrorException.class,
            () -> restClient.post()
                .uri(URL)
                .body(createLocationTypeRequest)
                .retrieve()
                .toEntity(SupplierResponse.class)
        );

        assertEquals(HttpStatus.CONFLICT, errorException.getStatusCode());
        assertTrue(errorException.getResponseBodyAsString().contains(LOCATION_TYPE_CODE_IS_NOT_UNIQUE.getCode()));
    }

    @CsvSource(value = "null, false", nullValues = "null")
    @ParameterizedTest()
    void locationTypeCreateErrorTestWhenWithEmptyDimensions(Boolean unlimited) {
        CreateLocationTypeRequest createLocationTypeRequest = new CreateLocationTypeRequest();
        createLocationTypeRequest.setCode(generateString());
        createLocationTypeRequest.setUnlimited(unlimited);

        HttpClientErrorException errorException = assertThrows(HttpClientErrorException.class,
            () -> restClient.post()
                .uri(URL)
                .body(createLocationTypeRequest)
                .retrieve()
                .toEntity(SupplierResponse.class)
        );

        assertEquals(HttpStatus.BAD_REQUEST, errorException.getStatusCode());
        assertTrue(errorException.getResponseBodyAsString().contains(CANT_CREATE_LOCATION_TYPE_WITHOUT_DIMENSIONS.getCode()));
    }

    @Test
    void locationTypeGetErrorTestWhenLocationTypeNotExist() {
        HttpClientErrorException errorException = assertThrows(HttpClientErrorException.class,
            () -> restClient.get()
                .uri(URL_BY_CODE.formatted(generateString()))
                .retrieve()
                .toEntity(LocationTypeResponse.class)
        );

        assertEquals(HttpStatus.NOT_FOUND, errorException.getStatusCode());
        assertTrue(errorException.getResponseBodyAsString().contains(LOCATION_TYPE_NOT_FOUND.getCode()));
    }

    @Test
    void locationTypeDeleteSuccessTest() {
        LocationTypeEntity locationTypeEntity = dataGenerator.generateLocationType();

        ResponseEntity<Void> response =
            restClient.delete().uri(URL_BY_CODE.formatted(locationTypeEntity.getCode())).retrieve().toBodilessEntity();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(locationTypeRepository.findByCode(locationTypeEntity.getCode()));
    }

    @Test
    void locationTypeDeleteErrorTestWhenLocationTypeNotFound() {
        HttpClientErrorException errorException = assertThrows(HttpClientErrorException.class,
            () -> restClient.delete()
                .uri(URL_BY_CODE.formatted(generateString()))
                .retrieve()
                .toEntity(LocationTypeResponse.class)
        );

        assertEquals(HttpStatus.NOT_FOUND, errorException.getStatusCode());
        assertTrue(errorException.getResponseBodyAsString().contains(LOCATION_TYPE_NOT_FOUND.getCode()));
    }

    @Test
    void locationTypeDeleteErrorTestWhenLocationExists() {
        LocationEntity locationEntity = dataGenerator.generateLocation(ZoneType.INBOUND);

        HttpClientErrorException errorException = assertThrows(HttpClientErrorException.class,
            () -> restClient.delete()
                .uri(URL_BY_CODE.formatted(locationEntity.getLocationType().getCode()))
                .retrieve()
                .toEntity(LocationTypeResponse.class)
        );

        assertEquals(HttpStatus.FORBIDDEN, errorException.getStatusCode());
        assertTrue(errorException.getResponseBodyAsString().contains(CANT_DELETE_LOCATION_TYPE_RELATED_TO_LOCATION.getCode()));
    }
}
