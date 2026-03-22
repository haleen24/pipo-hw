package ru.hse.pipo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.pipo.entity.ProductEntity;
import ru.hse.pipo.exception.InventoryException;
import ru.hse.pipo.exception.InventoryExceptionCode;
import ru.hse.pipo.mapper.ProductMapper;
import ru.hse.pipo.model.Product;
import ru.hse.pipo.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Product getByCode(String code) {
        ProductEntity productEntity = productRepository.findByCode(code);
        if (productEntity == null) {
            throw new InventoryException(InventoryExceptionCode.PRODUCT_NOT_FOUND, code);
        }
        return productMapper.toProduct(productEntity);
    }
}
