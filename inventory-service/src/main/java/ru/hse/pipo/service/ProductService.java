package ru.hse.pipo.service;

import ru.hse.pipo.model.Product;

public interface ProductService {
    Product create(Product product);

    Product getByCode(String code);
}
