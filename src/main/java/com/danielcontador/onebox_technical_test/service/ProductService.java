package com.danielcontador.onebox_technical_test.service;

import com.danielcontador.onebox_technical_test.dto.ProductDto;
import com.danielcontador.onebox_technical_test.entity.Product;

public interface ProductService {
    Product createProduct(ProductDto productDto);

    Product getProduct(long id);
}
