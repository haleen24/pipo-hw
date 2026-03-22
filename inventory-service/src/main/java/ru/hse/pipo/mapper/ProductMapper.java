package ru.hse.pipo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hse.inventory.model.ProductRequest;
import ru.hse.inventory.model.ProductResponse;
import ru.hse.pipo.entity.ProductEntity;
import ru.hse.pipo.model.Product;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product fromProductRequest(ProductRequest productRequest);

    ProductResponse toProductResponse(Product product);

    List<ProductResponse> toProductResponseList(List<Product> products);

    ProductEntity toProductEntity(Product product);

    Product fromProductEntity(ProductEntity productEntity);

    List<Product> fromProductEntities(List<ProductEntity> productEntities);
}
