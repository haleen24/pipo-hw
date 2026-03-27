package ru.hse.pipo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.inventory.controller.StockApi;
import ru.hse.inventory.model.MoveStockRequest;
import ru.hse.inventory.model.StockResponse;
import ru.hse.pipo.mapper.StockMapper;
import ru.hse.pipo.model.Stock;
import ru.hse.pipo.service.StockService;

@RestController
@RequiredArgsConstructor
public class StockController implements StockApi {
    private final StockService stockService;
    private final StockMapper stockMapper;


    @Override
    public ResponseEntity<StockResponse> moveToStock(MoveStockRequest moveStockRequest) {
        Stock stock =
            stockService.moveToStock(moveStockRequest.getLocationCodeTo(), moveStockRequest.getProductCode(), moveStockRequest.getAmount(),
                moveStockRequest.getShipmentUnitId());
        StockResponse stockResponse = stockMapper.toStockResponse(stock);
        return ResponseEntity.ok(stockResponse);
    }
}
