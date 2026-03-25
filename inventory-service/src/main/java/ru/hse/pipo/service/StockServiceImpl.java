package ru.hse.pipo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.pipo.entity.StockEntity;
import ru.hse.pipo.repository.StockRepository;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;

    @Override
    public Long getStockCountByProductCode(String productCode) {
        return stockRepository.findAllByProductCode(productCode).stream()
            .map(StockEntity::getAmount)
            .reduce(Long::sum)
            .orElse(0L);
    }
}
