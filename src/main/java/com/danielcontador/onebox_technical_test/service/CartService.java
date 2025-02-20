package com.danielcontador.onebox_technical_test.service;

import com.danielcontador.onebox_technical_test.dto.request.ProductDto;
import com.danielcontador.onebox_technical_test.dto.response.DeleteResponse;
import com.danielcontador.onebox_technical_test.entity.Cart;

import java.util.UUID;

public interface CartService {
    Cart createCart();

    Cart getCart(UUID cartId);

    DeleteResponse deleteCart(UUID cartId);

    Cart addProduct(UUID cartId, ProductDto productDto);

    void deleteInactiveCarts();
}
