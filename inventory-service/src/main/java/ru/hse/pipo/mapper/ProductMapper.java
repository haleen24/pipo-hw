package ru.hse.pipo.mapper;

import org.mapstruct.Mapper;
import ru.hse.pipo.entity.ProductEntity;
import ru.hse.pipo.model.Product;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ProductMapper {
    Product toProduct(ProductEntity productEntity);
}
