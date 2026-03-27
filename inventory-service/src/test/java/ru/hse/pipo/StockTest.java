package ru.hse.pipo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hse.inventory.model.MoveStockRequest;
import ru.hse.inventory.model.StockResponse;
import ru.hse.pipo.entity.LocationEntity;
import ru.hse.pipo.entity.ProductEntity;
import ru.hse.pipo.entity.ShipmentUnitEntity;
import ru.hse.pipo.model.ZoneType;
import ru.hse.pipo.utils.DataGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StockTest extends CommonTestConfiguration {
    final String URL = "/stock";

    @Autowired
    DataGenerator dataGenerator;

    @Test
    void moveToStockTest() {
        LocationEntity locationEntity = dataGenerator.generateLocation(ZoneType.STORAGE);
        ProductEntity productEntity = dataGenerator.generateProduct();
        ShipmentUnitEntity shipmentUnitEntity = dataGenerator.generateShipmentUnit();
        Long amount = shipmentUnitEntity.getAmount();
        MoveStockRequest moveStockRequest = new MoveStockRequest();
        moveStockRequest.setAmount(amount);
        moveStockRequest.setProductCode(productEntity.getCode());
        moveStockRequest.setShipmentUnitId(shipmentUnitEntity.getId());
        moveStockRequest.setLocationCodeTo(locationEntity.getCode());

        ResponseEntity<StockResponse> response = restClient.post().uri(URL).body(moveStockRequest).retrieve().toEntity(StockResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        StockResponse stockResponse = response.getBody();
        assertEquals(amount, stockResponse.getAmount());
        assertEquals(productEntity.getCode(), stockResponse.getProductCode());
        assertEquals(locationEntity.getCode(), stockResponse.getLocationCode());
    }
}
