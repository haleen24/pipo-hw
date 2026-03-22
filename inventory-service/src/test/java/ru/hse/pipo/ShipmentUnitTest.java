package ru.hse.pipo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import ru.hse.inventory.model.CreateShipmentUnitRequest;
import ru.hse.inventory.model.ShipmentUnitResponse;
import ru.hse.pipo.entity.LocationEntity;
import ru.hse.pipo.entity.ShipmentEntity;
import ru.hse.pipo.entity.ShipmentUnitEntity;
import ru.hse.pipo.exception.InventoryExceptionCode;
import ru.hse.pipo.model.ShipmentStatus;
import ru.hse.pipo.repository.ShipmentRepository;
import ru.hse.pipo.repository.ShipmentUnitRepository;
import ru.hse.pipo.utils.DataGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShipmentUnitTest extends CommonTestConfiguration {
    final String URL_BY_ID = "/shipmentUnit/%s";
    final String URL = "/shipmentUnit";

    @Autowired
    DataGenerator dataGenerator;

    @Autowired
    ShipmentUnitRepository shipmentUnitRepository;

    @Autowired
    ShipmentRepository shipmentRepository;

    @Test
    void getShipmentUnitNotFoundTest() {
        HttpClientErrorException errorException = assertThrows(HttpClientErrorException.class,
            () -> restClient.get()
                .uri(URL_BY_ID.formatted(random.nextInt()))
                .retrieve()
                .toBodilessEntity()
        );

        assertEquals(HttpStatus.NOT_FOUND, errorException.getStatusCode());
        String body = errorException.getResponseBodyAsString();
        assertTrue(body.contains(InventoryExceptionCode.SHIPMENT_UNIT_NOT_FOUND.getCode()));
    }

    @Test
    void getShipmentUnitSuccessTest() {
        ShipmentEntity shipmentEntity = dataGenerator.generateShipment();
        LocationEntity locationEntity = dataGenerator.generateLocation();
        ShipmentUnitEntity shipmentUnitEntity = ShipmentUnitEntity.builder()
            .shipment(shipmentEntity)
            .amount(generateLong())
            .location(locationEntity)
            .widthCm(generateLong())
            .heightCm(generateLong())
            .lengthCm(generateLong())
            .build();
        shipmentUnitRepository.save(shipmentUnitEntity);

        ResponseEntity<ShipmentUnitResponse> response = restClient.get()
            .uri(URL_BY_ID.formatted(shipmentUnitEntity.getId()))
            .retrieve()
            .toEntity(ShipmentUnitResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ShipmentUnitResponse shipmentUnitResponse = response.getBody();
        assertNotNull(shipmentUnitResponse);
        assertEquals(shipmentUnitEntity.getId(), shipmentUnitResponse.getId());
        assertEquals(shipmentUnitEntity.getAmount(), shipmentUnitResponse.getAmount());
        assertEquals(shipmentUnitEntity.getHeightCm(), shipmentUnitResponse.getHeightCm());
        assertEquals(shipmentUnitEntity.getWidthCm(), shipmentUnitResponse.getWidthCm());
        assertEquals(shipmentUnitEntity.getLengthCm(), shipmentUnitResponse.getLengthCm());
        assertEquals(shipmentUnitEntity.getLocation().getCode(), shipmentUnitResponse.getLocationCode());
        assertEquals(shipmentUnitEntity.getShipment().getId(), shipmentUnitResponse.getShipmentId());
    }

    @Test
    void createShipmentUnitSuccessTest() {
        ShipmentEntity shipmentEntity = dataGenerator.generateShipment();
        CreateShipmentUnitRequest createShipmentUnitRequest = new CreateShipmentUnitRequest();
        createShipmentUnitRequest.setShipmentId(shipmentEntity.getId());
        createShipmentUnitRequest.setAmount(generateLong());
        createShipmentUnitRequest.setHeightCm(generateLong());
        createShipmentUnitRequest.setLengthCm(generateLong());
        createShipmentUnitRequest.setWidthCm(generateLong());
        ResponseEntity<ShipmentUnitResponse> response =
            restClient.post().uri(URL).body(createShipmentUnitRequest).retrieve().toEntity(ShipmentUnitResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ShipmentUnitResponse shipmentResponse = response.getBody();
        assertNotNull(shipmentResponse);
        assertNotNull(shipmentResponse.getId());
        assertNull(shipmentResponse.getLocationCode());
        shipmentEntity = shipmentRepository.findById(shipmentEntity.getId()).orElseThrow();
        assertEquals(ShipmentStatus.IN_PROCESS.name(), shipmentEntity.getStatus());
    }
}
