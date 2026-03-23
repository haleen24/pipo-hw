package ru.hse.pipo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.inventory.controller.ProductApi;
import ru.hse.inventory.model.CreateProductRequest;
import ru.hse.inventory.model.ProductResponse;
import ru.hse.pipo.mapper.ProductMapper;
import ru.hse.pipo.model.Product;
import ru.hse.pipo.service.ProductService;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductApi {
    private final ProductService productService;
    private final ProductMapper productMapper;

    @Override
    public ResponseEntity<ProductResponse> createProduct(CreateProductRequest createProductRequest) {
        Product product = productMapper.toProduct(createProductRequest);
        Product createdProduct = productService.create(product);
        ProductResponse productResponse = productMapper.toProductResponse(createdProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    @Override
    public ResponseEntity<ProductResponse> getProduct(String code) {
        Product product = productService.getByCode(code);
        return ResponseEntity.ok(productMapper.toProductResponse(product));
    }
}
