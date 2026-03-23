package ru.hse.pipo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import ru.hse.inventory.model.CreateProductRequest;
import ru.hse.inventory.model.ProductResponse;
import ru.hse.inventory.model.SupplierResponse;
import ru.hse.pipo.entity.ProductEntity;
import ru.hse.pipo.utils.DataGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.hse.pipo.exception.InventoryExceptionCode.PRODUCT_CODE_NOT_UNIQUE;
import static ru.hse.pipo.exception.InventoryExceptionCode.PRODUCT_NOT_FOUND;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductTest extends CommonTestConfiguration {
    final String URL_BY_CODE = "/product/%s";
    final String URL = "/product";

    @Autowired
    DataGenerator dataGenerator;

    @Test
    void productCreateSuccessTest() {
        CreateProductRequest createProductRequest = new CreateProductRequest();
        createProductRequest.setCode(generateString());
        createProductRequest.setName(generateString());
        createProductRequest.setLengthCm(generateLong());
        createProductRequest.setWidthCm(generateLong());
        createProductRequest.setHeightCm(generateLong());

        ResponseEntity<ProductResponse> response =
            restClient.post().uri(URL).body(createProductRequest).retrieve().toEntity(ProductResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ProductResponse productResponse = response.getBody();
        assertNotNull(productResponse);
        assertNotNull(productResponse.getId());
        assertEquals(createProductRequest.getCode(), productResponse.getCode());
        assertEquals(createProductRequest.getName(), productResponse.getName());
        assertEquals(createProductRequest.getLengthCm(), productResponse.getLengthCm());
        assertEquals(createProductRequest.getWidthCm(), productResponse.getWidthCm());
        assertEquals(createProductRequest.getHeightCm(), productResponse.getHeightCm());
    }

    @Test
    void productGetSuccessTest() {
        ProductEntity productEntity = dataGenerator.generateProduct();

        ResponseEntity<ProductResponse> response =
            restClient.get().uri(URL_BY_CODE.formatted(productEntity.getCode())).retrieve().toEntity(ProductResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ProductResponse productResponse = response.getBody();
        assertNotNull(productResponse);
        assertNotNull(productResponse.getId());
        assertNotNull(productResponse.getCreatedAt());
        assertNotNull(productResponse.getUpdatedAt());
        assertEquals(productEntity.getCode(), productResponse.getCode());
        assertEquals(productEntity.getName(), productResponse.getName());
        assertEquals(productEntity.getLengthCm(), productResponse.getLengthCm());
        assertEquals(productEntity.getWidthCm(), productResponse.getWidthCm());
        assertEquals(productEntity.getHeightCm(), productResponse.getHeightCm());
    }

    @Test
    void productCreateErrorTestWhenCodeAlreadyExists() {
        ProductEntity productEntity = dataGenerator.generateProduct();
        CreateProductRequest createProductRequest = new CreateProductRequest();
        createProductRequest.setCode(productEntity.getCode());
        createProductRequest.setName(generateString());
        createProductRequest.setLengthCm(generateLong());
        createProductRequest.setWidthCm(generateLong());
        createProductRequest.setHeightCm(generateLong());

        HttpClientErrorException errorException = assertThrows(HttpClientErrorException.class,
            () -> restClient.post()
                .uri(URL)
                .body(createProductRequest)
                .retrieve()
                .toEntity(SupplierResponse.class)
        );

        assertEquals(HttpStatus.CONFLICT, errorException.getStatusCode());
        assertTrue(errorException.getResponseBodyAsString().contains(PRODUCT_CODE_NOT_UNIQUE.getCode()));
    }

    @Test
    void productGetErrorTestWhenSupplierNotExist() {
        HttpClientErrorException errorException = assertThrows(HttpClientErrorException.class,
            () -> restClient.get()
                .uri(URL_BY_CODE.formatted(generateString()))
                .retrieve()
                .toEntity(ProductResponse.class)
        );

        assertEquals(HttpStatus.NOT_FOUND, errorException.getStatusCode());
        assertTrue(errorException.getResponseBodyAsString().contains(PRODUCT_NOT_FOUND.getCode()));
    }
}
