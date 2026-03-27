package ru.hse.pipo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hse.inventory.model.StockResponse;
import ru.hse.pipo.entity.StockEntity;
import ru.hse.pipo.model.Stock;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface StockMapper {
    StockEntity toStockEntity(Stock stock);

    Stock toStock(StockEntity stockEntity);

    @Mapping(source = "location.code", target = "locationCode")
    @Mapping(source = "product.code", target = "productCode")
    StockResponse toStockResponse(Stock stock);
}
