package ru.hse.pipo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import ru.hse.inventory.model.CreateSupplierRequest;
import ru.hse.inventory.model.PatchSupplierRequest;
import ru.hse.inventory.model.SupplierResponse;
import ru.hse.pipo.entity.SupplierEntity;
import ru.hse.pipo.exception.InventoryExceptionCode;
import ru.hse.pipo.repository.SupplierRepository;
import ru.hse.pipo.utils.DataGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SupplierTest extends CommonTestConfiguration {
    final String URL_BY_CODE = "/supplier/%s";
    final String URL = "/supplier";

    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    DataGenerator dataGenerator;

    @Test
    void supplierCreateSuccessTest() {
        CreateSupplierRequest createSupplierRequest = new CreateSupplierRequest();
        createSupplierRequest.setCode(generateString());
        createSupplierRequest.setName(generateString());

        ResponseEntity<SupplierResponse> response =
            restClient.post().uri(URL).body(createSupplierRequest).retrieve().toEntity(SupplierResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        SupplierResponse supplierResponse = response.getBody();
        assertNotNull(supplierResponse);
        assertNotNull(supplierResponse.getId());
        assertEquals(createSupplierRequest.getCode(), supplierResponse.getCode());
        assertEquals(createSupplierRequest.getName(), supplierResponse.getName());
        assertNotNull(supplierRepository.findById(supplierResponse.getId()).orElse(null));
    }

    @Test
    void supplierGetSuccessTest() {
        SupplierEntity supplierEntity = dataGenerator.generateSupplier();

        ResponseEntity<SupplierResponse> response =
            restClient.get().uri(URL_BY_CODE.formatted(supplierEntity.getCode())).retrieve().toEntity(SupplierResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        SupplierResponse supplierResponse = response.getBody();
        assertNotNull(supplierResponse);
        assertNotNull(supplierResponse.getId());
        assertNotNull(supplierResponse.getCreatedAt());
        assertNotNull(supplierResponse.getUpdatedAt());
        assertEquals(supplierEntity.getCode(), supplierResponse.getCode());
        assertEquals(supplierEntity.getName(), supplierResponse.getName());
    }

    @Test
    void supplierPatchSuccessTest() {
        SupplierEntity supplierEntity = dataGenerator.generateSupplier();
        CreateSupplierRequest createSupplierRequest = new CreateSupplierRequest();
        createSupplierRequest.setCode(generateString());
        createSupplierRequest.setName(generateString());

        ResponseEntity<SupplierResponse> response =
            restClient.patch().uri(URL_BY_CODE.formatted(supplierEntity.getCode())).body(createSupplierRequest).retrieve()
                .toEntity(SupplierResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        SupplierResponse supplierResponse = response.getBody();
        assertNotNull(supplierResponse);
        assertNotNull(supplierResponse.getId());
        assertEquals(createSupplierRequest.getCode(), supplierResponse.getCode());
        assertEquals(createSupplierRequest.getName(), supplierResponse.getName());
        SupplierEntity patchedSupplierEntity = supplierRepository.findById(supplierResponse.getId()).orElse(null);
        assertNotNull(patchedSupplierEntity);
        assertEquals(createSupplierRequest.getCode(), patchedSupplierEntity.getCode());
        assertEquals(createSupplierRequest.getName(), patchedSupplierEntity.getName());
    }

    @Test
    void supplierCreateErrorTestWhenCodeAlreadyExists() {
        SupplierEntity supplierEntity = dataGenerator.generateSupplier();
        CreateSupplierRequest createSupplierRequest = new CreateSupplierRequest();
        createSupplierRequest.setCode(supplierEntity.getCode());
        createSupplierRequest.setName(supplierEntity.getName());

        HttpClientErrorException errorException = assertThrows(HttpClientErrorException.class,
            () -> restClient.post()
                .uri(URL)
                .body(createSupplierRequest)
                .retrieve()
                .toEntity(SupplierResponse.class)
        );

        assertEquals(HttpStatus.CONFLICT, errorException.getStatusCode());
        assertTrue(errorException.getResponseBodyAsString().contains(InventoryExceptionCode.SUPPLIER_CODE_NOT_UNIQUE.getCode()));
    }

    @Test
    void supplierPatchErrorTestWhenCodeAlreadyExists() {
        SupplierEntity firstSupplierEntity = dataGenerator.generateSupplier();
        SupplierEntity secondSupplierEntity = dataGenerator.generateSupplier();
        PatchSupplierRequest patchSupplierRequest = new PatchSupplierRequest();
        patchSupplierRequest.setCode(firstSupplierEntity.getCode());

        HttpClientErrorException errorException = assertThrows(HttpClientErrorException.class,
            () -> restClient.patch()
                .uri(URL_BY_CODE.formatted(secondSupplierEntity.getCode()))
                .body(patchSupplierRequest)
                .retrieve()
                .toEntity(SupplierResponse.class)
        );

        assertEquals(HttpStatus.CONFLICT, errorException.getStatusCode());
        assertTrue(errorException.getResponseBodyAsString().contains(InventoryExceptionCode.SUPPLIER_CODE_NOT_UNIQUE.getCode()));
    }

    @Test
    void supplierGetErrorTestWhenSupplierNotExist() {
        HttpClientErrorException errorException = assertThrows(HttpClientErrorException.class,
            () -> restClient.get()
                .uri(URL_BY_CODE.formatted(generateString()))
                .retrieve()
                .toEntity(SupplierResponse.class)
        );

        assertEquals(HttpStatus.NOT_FOUND, errorException.getStatusCode());
        assertTrue(errorException.getResponseBodyAsString().contains(InventoryExceptionCode.SUPPLIER_NOT_FOUND.getCode()));
    }
}

