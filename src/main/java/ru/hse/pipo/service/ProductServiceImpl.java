package ru.hse.pipo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.pipo.entity.ProductEntity;
import ru.hse.pipo.mapper.ProductMapper;
import ru.hse.pipo.model.Product;
import ru.hse.pipo.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Product createProduct(Product product) {
        ProductEntity productEntity = productMapper.toProductEntity(product);
        ProductEntity createdProductEntity = productRepository.save(productEntity);
        return productMapper.fromProductEntity(createdProductEntity);
    }

    @Override
    public List<Product> getProducts() {
        List<ProductEntity> productEntities = productRepository.findAll();
        return productMapper.fromProductEntities(productEntities);
    }
}
