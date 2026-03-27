package ru.hse.pipo.service;

import java.util.List;

import ru.hse.pipo.model.Stock;

public interface StockService {
    Long getStockCountByProductCode(String productCode);

    List<Stock> getStockByLocationCode(String locationCode);

    void update(Stock stock);

    Stock moveToStock(String locationCode, String productCode, Long amount, Long shipmentUnitId);
}
