package ru.hse.pipo.service;

import java.util.List;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.pipo.entity.StockEntity;
import ru.hse.pipo.exception.InventoryException;
import ru.hse.pipo.exception.InventoryExceptionCode;
import ru.hse.pipo.mapper.StockMapper;
import ru.hse.pipo.model.Location;
import ru.hse.pipo.model.LocationType;
import ru.hse.pipo.model.Product;
import ru.hse.pipo.model.ShipmentUnit;
import ru.hse.pipo.model.Stock;
import ru.hse.pipo.model.ZoneType;
import ru.hse.pipo.repository.StockRepository;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;
    private final StockMapper stockMapper;
    private final LocationService locationService;
    private final ProductService productService;
    private final ShipmentUnitService shipmentUnitService;

    @Override
    public Long getStockCountByProductCode(String productCode) {
        return stockRepository.findAllByProductCode(productCode).stream()
            .map(StockEntity::getAmount)
            .reduce(Long::sum)
            .orElse(0L);
    }

    @Override
    public List<Stock> getStockByLocationCode(String locationCode) {
        return stockRepository.findAllByLocationCode(locationCode).stream()
            .map(stockMapper::toStock)
            .toList();
    }

    @Override
    public void update(Stock stock) {
        stockRepository.save(stockMapper.toStockEntity(stock));
    }

    @Override
    @Transactional
    public Stock moveToStock(String locationCode, String productCode, Long amount, Long shipmentUnitId) {
        Location location = locationService.getByCode(locationCode);
        Product product = productService.getByCode(productCode);
        validateLocation(product, amount, location);
        Long actualAmount = amount;
        if (shipmentUnitId != null) {
            ShipmentUnit shipmentUnit = shipmentUnitService.getById(shipmentUnitId);
            actualAmount = Math.min(shipmentUnit.getAmount(), amount);
        }
        Stock stock = Stock.builder()
            .location(location)
            .product(product)
            .amount(actualAmount)
            .build();
        stockRepository.save(stockMapper.toStockEntity(stock));
        return stock;
    }

    void validateLocation(Product product, Long amount, Location location) {
        if (location.getZone().getType() != ZoneType.STORAGE) {
            throw new InventoryException(InventoryExceptionCode.LOCATION_NOT_IN_STORAGE);
        }
        LocationType locationType = location.getLocationType();
        if (locationType.getUnlimited()) {
            return;
        }
        long productVolume = product.getHeightCm() * product.getWidthCm() * product.getLengthCm() * amount;
        List<Stock> currentStockInLocation = getStockByLocationCode(location.getCode());
        long currentLocationVolume = currentStockInLocation.stream()
            .mapToLong(stock -> stock.getProduct().getHeightCm() * stock.getProduct().getWidthCm() * stock.getProduct().getLengthCm() *
                stock.getAmount())
            .sum();
        long maximumLocationVolume =
            locationType.getHeightCm() * locationType.getWidthCm() * locationType.getLengthCm();
        if (productVolume + currentLocationVolume > maximumLocationVolume) {
            throw new InventoryException(InventoryExceptionCode.NO_ROOM_FOR_STOCK_IN_LOCATION, location.getCode(), product.getCode(),
                amount.toString());
        }
    }
}
