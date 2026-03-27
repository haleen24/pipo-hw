package ru.hse.pipo.mapper;

import org.mapstruct.Mapper;
import ru.hse.pipo.entity.StockEntity;
import ru.hse.pipo.model.Stock;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface StockMapper {
    StockEntity toStockEntity(Stock stock);

    Stock toStock(StockEntity stockEntity);
}
