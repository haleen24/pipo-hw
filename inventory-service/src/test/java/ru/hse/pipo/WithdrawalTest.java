package ru.hse.pipo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import ru.hse.inventory.model.ProcessWithdrawalRequest;
import ru.hse.inventory.model.WithdrawalResponse;
import ru.hse.pipo.entity.OutboundShipmentEntity;
import ru.hse.pipo.entity.StockEntity;
import ru.hse.pipo.entity.WithdrawalEntity;
import ru.hse.pipo.model.OutboundShipmentStatus;
import ru.hse.pipo.model.WithdrawalStatus;
import ru.hse.pipo.repository.OutboundShipmentRepository;
import ru.hse.pipo.utils.DataGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.hse.pipo.exception.InventoryExceptionCode.OUTBOUND_SHIPMENT_CANCELED;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WithdrawalTest extends CommonTestConfiguration {
    final String URL_BY_ID = "/withdrawal/%s";

    @Autowired
    DataGenerator dataGenerator;

    @Autowired
    OutboundShipmentRepository outboundShipmentRepository;

    @Test
    void processWithdrawalSuccessTest() {
        OutboundShipmentEntity outboundShipmentEntity = dataGenerator.generateOutboundShipment();
        WithdrawalEntity withdrawalEntity = dataGenerator.generateWithdrawal(outboundShipmentEntity);
        Long productCount = withdrawalEntity.getAmount();
        StockEntity stockEntity = dataGenerator.generateStock(withdrawalEntity.getProduct(), productCount);
        ProcessWithdrawalRequest processWithdrawalRequest = new ProcessWithdrawalRequest();
        processWithdrawalRequest.setLocationCode(stockEntity.getLocation().getCode());

        ResponseEntity<WithdrawalResponse> response =
            restClient.put().uri(URL_BY_ID.formatted(withdrawalEntity.getId())).body(processWithdrawalRequest).retrieve()
                .toEntity(WithdrawalResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        WithdrawalResponse withdrawalResponse = response.getBody();
        assertNotNull(withdrawalResponse);
        assertEquals(withdrawalEntity.getId(), withdrawalResponse.getId());
        assertEquals(withdrawalEntity.getProduct().getCode(), withdrawalResponse.getProductCode());
        assertEquals(withdrawalEntity.getAmount(), withdrawalResponse.getProductCount());
        assertEquals(outboundShipmentEntity.getId(), withdrawalResponse.getOutboundShipmentId());
        assertEquals(WithdrawalStatus.COMPLETED.name(), withdrawalResponse.getStatus());
        outboundShipmentEntity = outboundShipmentRepository.findById(outboundShipmentEntity.getId()).orElseThrow();
        assertEquals(WithdrawalStatus.COMPLETED.name(), outboundShipmentEntity.getStatus());
    }

    @Test
    void processWithdrawalErrorTestWhenOutboundShipmentCancelled() {
        OutboundShipmentEntity outboundShipmentEntity = dataGenerator.generateOutboundShipment();
        outboundShipmentEntity.setStatus(OutboundShipmentStatus.CANCELED.name());
        outboundShipmentRepository.save(outboundShipmentEntity);
        WithdrawalEntity withdrawalEntity = dataGenerator.generateWithdrawal(outboundShipmentEntity);
        Long productCount = withdrawalEntity.getAmount();
        StockEntity stockEntity = dataGenerator.generateStock(withdrawalEntity.getProduct(), productCount);
        ProcessWithdrawalRequest processWithdrawalRequest = new ProcessWithdrawalRequest();
        processWithdrawalRequest.setLocationCode(stockEntity.getLocation().getCode());

        HttpClientErrorException errorException = assertThrows(HttpClientErrorException.class,
            () -> restClient.put()
                .uri(URL_BY_ID.formatted(withdrawalEntity.getId()))
                .body(processWithdrawalRequest)
                .retrieve()
                .toEntity(WithdrawalResponse.class)
        );

        assertEquals(HttpStatus.BAD_REQUEST, errorException.getStatusCode());
        assertTrue(errorException.getResponseBodyAsString().contains(OUTBOUND_SHIPMENT_CANCELED.getCode()));
    }
}
