package ru.hse.pipo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import ru.hse.inventory.model.CreateShipmentRequest;
import ru.hse.inventory.model.ShipmentResponse;
import ru.hse.pipo.entity.ProductEntity;
import ru.hse.pipo.entity.ShipmentEntity;
import ru.hse.pipo.entity.SupplierEntity;
import ru.hse.pipo.exception.InventoryExceptionCode;
import ru.hse.pipo.model.ShipmentStatus;
import ru.hse.pipo.repository.ShipmentRepository;
import ru.hse.pipo.utils.DataGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShipmentTest extends CommonTestConfiguration {
    final String URL_BY_ID = "/shipment/%s";
    final String URL = "/shipment";

    @Autowired
    DataGenerator dataGenerator;

    @Autowired
    ShipmentRepository shipmentRepository;

    @Test
    void getShipmentNotFoundTest() {
        HttpClientErrorException errorException = assertThrows(HttpClientErrorException.class,
            () -> restClient.get()
                .uri(URL_BY_ID.formatted(random.nextInt()))
                .retrieve()
                .toBodilessEntity()
        );

        assertEquals(HttpStatus.NOT_FOUND, errorException.getStatusCode());
        String body = errorException.getResponseBodyAsString();
        assertTrue(body.contains(InventoryExceptionCode.SHIPMENT_NOT_FOUND.getCode()));
    }

    @Test
    void getShipmentSuccessTest() {
        SupplierEntity supplierEntity = dataGenerator.generateSupplier();
        ProductEntity productEntity = dataGenerator.generateProduct();
        ShipmentEntity shipmentEntity = ShipmentEntity.builder()
            .supplier(supplierEntity)
            .externalShipmentId(generateString())
            .product(productEntity)
            .status(ShipmentStatus.IN_PROCESS.name())
            .shipmentUnitCount(generateLong())
            .build();
        shipmentRepository.save(shipmentEntity);


        ResponseEntity<ShipmentResponse> response = restClient.get()
            .uri(URL_BY_ID.formatted(shipmentEntity.getId()))
            .retrieve()
            .toEntity(ShipmentResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ShipmentResponse shipmentResponse = response.getBody();
        assertNotNull(shipmentResponse);
        assertEquals(shipmentEntity.getId(), shipmentResponse.getId());
        assertEquals(shipmentEntity.getExternalShipmentId(), shipmentResponse.getExternalShipmentId());
        assertEquals(shipmentEntity.getStatus(), shipmentResponse.getStatus());
        assertEquals(shipmentEntity.getProduct().getCode(), shipmentResponse.getProductCode());
        assertNotNull(shipmentResponse.getUpdatedAt());
        assertNotNull(shipmentResponse.getCreatedAt());
    }

    @Test
    void createShipmentSuccessTest() {
        SupplierEntity supplierEntity = dataGenerator.generateSupplier();
        ProductEntity productEntity = dataGenerator.generateProduct();
        CreateShipmentRequest createShipmentRequest = new CreateShipmentRequest();
        createShipmentRequest.setSupplierCode(supplierEntity.getCode());
        createShipmentRequest.setProductCode(productEntity.getCode());
        createShipmentRequest.externalShipmentId(generateString());
        createShipmentRequest.setShipmentUnitCount(generateLong());

        ResponseEntity<ShipmentResponse> response =
            restClient.post().uri(URL).body(createShipmentRequest).retrieve().toEntity(ShipmentResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ShipmentResponse shipmentResponse = response.getBody();
        assertNotNull(shipmentResponse);
        assertNotNull(shipmentResponse.getId());
        assertEquals(createShipmentRequest.getShipmentUnitCount(), shipmentResponse.getShipmentUnitCount());
        assertEquals(ShipmentStatus.CREATED.name(), shipmentResponse.getStatus());
    }
}
