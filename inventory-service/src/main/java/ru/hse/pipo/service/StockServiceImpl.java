package ru.hse.pipo.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.pipo.entity.StockEntity;
import ru.hse.pipo.mapper.StockMapper;
import ru.hse.pipo.model.Stock;
import ru.hse.pipo.repository.StockRepository;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

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
}
