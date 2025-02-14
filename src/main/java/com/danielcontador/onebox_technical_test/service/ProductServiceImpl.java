package com.danielcontador.onebox_technical_test.service;

import com.danielcontador.onebox_technical_test.dto.ProductDto;
import com.danielcontador.onebox_technical_test.entity.Product;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class ProductServiceImpl implements ProductService {

    private final Map<Long, Product> products = new HashMap<>();

    @Override
    public Product createProduct(ProductDto productDto) {
        Product product = new Product(productDto.description(), productDto.amount());
        products.put(product.getId(), product);
        return product;
    }

    @Override
    public Product getProduct(long id) {
        Product product = products.get(id);
        if (product == null) {
            throw new NoSuchElementException("Product not found with ID: " + id);
        }
        return product;
    }
}
