package ru.hse.pipo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import ru.hse.inventory.model.CreateReceiverRequest;
import ru.hse.inventory.model.ReceiverResponse;
import ru.hse.pipo.entity.ReceiverEntity;
import ru.hse.pipo.exception.InventoryExceptionCode;
import ru.hse.pipo.repository.ReceiverRepository;
import ru.hse.pipo.utils.DataGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReceiverTest extends CommonTestConfiguration {
    final String URL_BY_CODE = "/receiver/%s";
    final String URL = "/receiver";

    @Autowired
    ReceiverRepository receiverRepository;

    @Autowired
    DataGenerator dataGenerator;

    @Test
    void receiverCreateSuccessTest() {
        CreateReceiverRequest createReceiverRequest = new CreateReceiverRequest();
        createReceiverRequest.setCode(generateString());
        createReceiverRequest.setName(generateString());

        ResponseEntity<ReceiverResponse> response =
            restClient.post().uri(URL).body(createReceiverRequest).retrieve().toEntity(ReceiverResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ReceiverResponse receiverResponse = response.getBody();
        assertNotNull(receiverResponse);
        assertNotNull(receiverResponse.getId());
        assertEquals(createReceiverRequest.getCode(), receiverResponse.getCode());
        assertEquals(createReceiverRequest.getName(), receiverResponse.getName());
        assertNotNull(receiverRepository.findById(receiverResponse.getId()).orElse(null));
    }

    @Test
    void receiverGetSuccessTest() {
        ReceiverEntity receiverEntity = dataGenerator.generateReceiver();

        ResponseEntity<ReceiverResponse> response =
            restClient.get().uri(URL_BY_CODE.formatted(receiverEntity.getCode())).retrieve().toEntity(ReceiverResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ReceiverResponse receiverResponse = response.getBody();
        assertNotNull(receiverResponse);
        assertNotNull(receiverResponse.getId());
        assertNotNull(receiverResponse.getCreatedAt());
        assertNotNull(receiverResponse.getUpdatedAt());
        assertEquals(receiverEntity.getCode(), receiverResponse.getCode());
        assertEquals(receiverEntity.getName(), receiverResponse.getName());
    }

    @Test
    void receiverCreateErrorTestWhenCodeAlreadyExists() {
        ReceiverEntity receiverEntity = dataGenerator.generateReceiver();
        CreateReceiverRequest createReceiverRequest = new CreateReceiverRequest();
        createReceiverRequest.setCode(receiverEntity.getCode());
        createReceiverRequest.setName(receiverEntity.getName());

        HttpClientErrorException errorException = assertThrows(HttpClientErrorException.class,
            () -> restClient.post()
                .uri(URL)
                .body(createReceiverRequest)
                .retrieve()
                .toEntity(ReceiverResponse.class)
        );

        assertEquals(HttpStatus.CONFLICT, errorException.getStatusCode());
        assertTrue(errorException.getResponseBodyAsString().contains(InventoryExceptionCode.RECEIVER_CODE_IS_NOT_UNIQUE.getCode()));
    }

    @Test
    void receiverGetErrorTestWhenReceiverNotExist() {
        HttpClientErrorException errorException = assertThrows(HttpClientErrorException.class,
            () -> restClient.get()
                .uri(URL_BY_CODE.formatted(generateString()))
                .retrieve()
                .toEntity(ReceiverResponse.class)
        );

        assertEquals(HttpStatus.NOT_FOUND, errorException.getStatusCode());
        assertTrue(errorException.getResponseBodyAsString().contains(InventoryExceptionCode.RECEIVER_NOT_FOUND.getCode()));
    }
}

