package ru.hse.pipo.service;

import ru.hse.pipo.model.Product;

import java.util.List;

public interface ProductService {
    Product createProduct(Product product);

    List<Product> getProducts();
}
