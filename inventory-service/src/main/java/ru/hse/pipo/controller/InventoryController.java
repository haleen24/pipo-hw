package ru.hse.pipo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.inventory.controller.InventoryApi;
import ru.hse.inventory.model.ProductRequest;
import ru.hse.inventory.model.ProductResponse;
import ru.hse.pipo.mapper.ProductMapper;
import ru.hse.pipo.model.Product;
import ru.hse.pipo.service.ProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InventoryController implements InventoryApi {
    private final ProductMapper productMapper;
    private final ProductService productService;

    @Override
    public ResponseEntity<ProductResponse> createProduct(ProductRequest productRequest) {
        Product product = productMapper.fromProductRequest(productRequest);
        Product createdProduct = productService.createProduct(product);
        ProductResponse productResponse = productMapper.toProductResponse(createdProduct);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(productResponse);
    }

    @Override
    public ResponseEntity<List<ProductResponse>> getProducts() {
        List<Product> products = productService.getProducts();
        List<ProductResponse> productResponseList = productMapper.toProductResponseList(products);
        return ResponseEntity.ok(productResponseList);
    }
}
