package ru.hse.pipo;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import ru.hse.inventory.model.CreateOutboundShipmentRequest;
import ru.hse.inventory.model.CreateOutboundShipmentRequestProductsInner;
import ru.hse.inventory.model.OutboundShipmentResponse;
import ru.hse.inventory.model.WithdrawalResponse;
import ru.hse.pipo.entity.OutboundShipmentEntity;
import ru.hse.pipo.entity.ProductEntity;
import ru.hse.pipo.entity.ReceiverEntity;
import ru.hse.pipo.entity.WithdrawalEntity;
import ru.hse.pipo.exception.InventoryExceptionCode;
import ru.hse.pipo.model.OutboundShipmentStatus;
import ru.hse.pipo.model.WithdrawalStatus;
import ru.hse.pipo.repository.OutboundShipmentRepository;
import ru.hse.pipo.utils.DataGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OutboundOutboundShipmentTest extends CommonTestConfiguration {
    final String URL_BY_ID = "/outboundShipment/%s";
    final String URL = "/outboundShipment";

    @Autowired
    DataGenerator dataGenerator;

    @Autowired
    OutboundShipmentRepository outboundShipmentRepository;

    @Test
    void getOutboundShipmentNotFoundTest() {
        HttpClientErrorException errorException = assertThrows(HttpClientErrorException.class,
            () -> restClient.get()
                .uri(URL_BY_ID.formatted(random.nextInt()))
                .retrieve()
                .toBodilessEntity()
        );

        assertEquals(HttpStatus.NOT_FOUND, errorException.getStatusCode());
        String body = errorException.getResponseBodyAsString();
        assertTrue(body.contains(InventoryExceptionCode.OUTBOUND_SHIPMENT_NOT_FOUND.getCode()));
    }

    @Test
    void getOutboundShipmentSuccessTest() {
        ReceiverEntity receiverEntity = dataGenerator.generateReceiver();
        OutboundShipmentEntity outboundOutboundShipmentEntity = OutboundShipmentEntity.builder()
            .receiver(receiverEntity)
            .status(OutboundShipmentStatus.IN_PROCESS.name())
            .build();
        outboundShipmentRepository.save(outboundOutboundShipmentEntity);
        WithdrawalEntity withdrawalEntity = dataGenerator.generateWithdrawal(outboundOutboundShipmentEntity);

        ResponseEntity<OutboundShipmentResponse> response = restClient.get()
            .uri(URL_BY_ID.formatted(outboundOutboundShipmentEntity.getId()))
            .retrieve()
            .toEntity(OutboundShipmentResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        OutboundShipmentResponse outboundOutboundShipmentResponse = response.getBody();
        assertNotNull(outboundOutboundShipmentResponse);
        assertEquals(outboundOutboundShipmentEntity.getId(), outboundOutboundShipmentResponse.getId());
        assertEquals(outboundOutboundShipmentEntity.getStatus(), outboundOutboundShipmentResponse.getStatus());
        assertEquals(outboundOutboundShipmentEntity.getReceiver().getCode(), outboundOutboundShipmentResponse.getReceiverCode());
        assertEquals(1, outboundOutboundShipmentResponse.getWithdrawals().size());
        WithdrawalResponse withdrawalResponse = outboundOutboundShipmentResponse.getWithdrawals().getFirst();
        assertEquals(outboundOutboundShipmentResponse.getId(), withdrawalResponse.getOutboundShipmentId());
        assertEquals(withdrawalEntity.getAmount(), withdrawalResponse.getProductCount());
        assertEquals(withdrawalEntity.getProduct().getCode(), withdrawalResponse.getProductCode());
        assertNotNull(outboundOutboundShipmentResponse.getUpdatedAt());
        assertNotNull(outboundOutboundShipmentResponse.getCreatedAt());
    }

    @Test
    void createOutboundShipmentSuccessTest() {
        ReceiverEntity receiverEntity = dataGenerator.generateReceiver();
        ProductEntity productEntity = dataGenerator.generateProduct();
        Long productCount = 10L;
        dataGenerator.generateStock(productEntity, 100L);
        CreateOutboundShipmentRequest createOutboundShipmentRequest =
            getCreateOutboundShipmentRequest(receiverEntity, productEntity, productCount);

        ResponseEntity<OutboundShipmentResponse> response =
            restClient.post().uri(URL).body(createOutboundShipmentRequest).retrieve().toEntity(OutboundShipmentResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        OutboundShipmentResponse outboundOutboundShipmentResponse = response.getBody();
        assertNotNull(outboundOutboundShipmentResponse);
        assertNotNull(outboundOutboundShipmentResponse.getId());
        assertEquals(outboundOutboundShipmentResponse.getStatus(), OutboundShipmentStatus.IN_PROCESS.name());
        assertEquals(outboundOutboundShipmentResponse.getReceiverCode(), receiverEntity.getCode());
        assertEquals(1, outboundOutboundShipmentResponse.getWithdrawals().size());
        WithdrawalResponse withdrawalResponse = outboundOutboundShipmentResponse.getWithdrawals().getFirst();
        assertEquals(outboundOutboundShipmentResponse.getId(), withdrawalResponse.getOutboundShipmentId());
        assertEquals(productCount, withdrawalResponse.getProductCount());
        assertEquals(productEntity.getCode(), withdrawalResponse.getProductCode());
        assertNotNull(outboundOutboundShipmentResponse.getUpdatedAt());
        assertNotNull(outboundOutboundShipmentResponse.getCreatedAt());
    }

    @Test
    void createOutboundShipmentErrorTestWhenNotEnoughStock() {
        ReceiverEntity receiverEntity = dataGenerator.generateReceiver();
        ProductEntity productEntity = dataGenerator.generateProduct();
        dataGenerator.generateStock(productEntity, 10L);
        CreateOutboundShipmentRequest createOutboundShipmentRequest =
            getCreateOutboundShipmentRequest(receiverEntity, productEntity, 100L);

        HttpClientErrorException errorException = assertThrows(HttpClientErrorException.class,
            () -> restClient.post()
                .uri(URL)
                .body(createOutboundShipmentRequest)
                .retrieve()
                .toBodilessEntity()
        );

        assertEquals(HttpStatus.BAD_REQUEST, errorException.getStatusCode());
        String body = errorException.getResponseBodyAsString();
        assertTrue(body.contains(InventoryExceptionCode.NOT_ENOUGH_STOCK.getCode()));
    }

    @Test
    void failOutboundShipmentSuccessTest() {
        OutboundShipmentEntity outboundOutboundShipmentEntity = dataGenerator.generateOutboundShipment();
        dataGenerator.generateWithdrawal(outboundOutboundShipmentEntity);

        ResponseEntity<OutboundShipmentResponse> response =
            restClient.put().uri(URL_BY_ID.formatted(outboundOutboundShipmentEntity.getId())).retrieve()
                .toEntity(OutboundShipmentResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        OutboundShipmentResponse outboundOutboundShipmentResponse = response.getBody();
        assertNotNull(outboundOutboundShipmentResponse);
        assertNotNull(outboundOutboundShipmentResponse.getId());
        assertEquals(outboundOutboundShipmentResponse.getStatus(), OutboundShipmentStatus.CANCELED.name());
        assertEquals(outboundOutboundShipmentResponse.getReceiverCode(), outboundOutboundShipmentEntity.getReceiver().getCode());
        assertEquals(1, outboundOutboundShipmentResponse.getWithdrawals().size());
        WithdrawalResponse withdrawalResponse = outboundOutboundShipmentResponse.getWithdrawals().getFirst();
        assertEquals(WithdrawalStatus.FAILED.name(),withdrawalResponse.getStatus());
        assertNotNull(outboundOutboundShipmentResponse.getUpdatedAt());
        assertNotNull(outboundOutboundShipmentResponse.getCreatedAt());
    }

    private static @NotNull CreateOutboundShipmentRequest getCreateOutboundShipmentRequest(ReceiverEntity receiverEntity,
                                                                                           ProductEntity productEntity, Long productCount) {
        CreateOutboundShipmentRequest createOutboundShipmentRequest = new CreateOutboundShipmentRequest();
        createOutboundShipmentRequest.setReceiverCode(receiverEntity.getCode());
        CreateOutboundShipmentRequestProductsInner createOutboundShipmentRequestProductsInner =
            new CreateOutboundShipmentRequestProductsInner();
        createOutboundShipmentRequestProductsInner.setProductCode(productEntity.getCode());
        createOutboundShipmentRequestProductsInner.setProductCount(productCount);
        createOutboundShipmentRequest.setProducts(List.of(createOutboundShipmentRequestProductsInner));
        return createOutboundShipmentRequest;
    }
}
