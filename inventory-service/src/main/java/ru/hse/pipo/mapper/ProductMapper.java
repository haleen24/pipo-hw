package ru.hse.pipo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hse.inventory.model.CreateProductRequest;
import ru.hse.inventory.model.ProductResponse;
import ru.hse.pipo.entity.ProductEntity;
import ru.hse.pipo.model.Product;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ProductMapper {
    Product toProduct(ProductEntity productEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toProduct(CreateProductRequest productEntity);

    ProductResponse toProductResponse(Product product);

    ProductEntity toProductEntity(Product product);
}
