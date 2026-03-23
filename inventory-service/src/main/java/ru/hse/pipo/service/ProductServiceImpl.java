package ru.hse.pipo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.pipo.entity.ProductEntity;
import ru.hse.pipo.exception.InventoryException;
import ru.hse.pipo.mapper.ProductMapper;
import ru.hse.pipo.model.Product;
import ru.hse.pipo.repository.ProductRepository;

import static ru.hse.pipo.exception.InventoryExceptionCode.PRODUCT_CODE_NOT_UNIQUE;
import static ru.hse.pipo.exception.InventoryExceptionCode.PRODUCT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Product create(Product product) {
        validateCode(product.getCode());
        ProductEntity productEntity = productMapper.toProductEntity(product);
        ProductEntity createdProductEntity = productRepository.save(productEntity);
        return productMapper.toProduct(createdProductEntity);
    }

    @Override
    public Product getByCode(String code) {
        ProductEntity productEntity = findProductEntityByCode(code);
        if (productEntity == null) {
            throw new InventoryException(PRODUCT_NOT_FOUND, code);
        }
        return productMapper.toProduct(productEntity);
    }

    private void validateCode(String code) {
        ProductEntity productEntity = findProductEntityByCode(code);
        if (productEntity != null) {
            throw new InventoryException(PRODUCT_CODE_NOT_UNIQUE);
        }
    }

    private ProductEntity findProductEntityByCode(String code) {
        return productRepository.findByCode(code);
    }
}
