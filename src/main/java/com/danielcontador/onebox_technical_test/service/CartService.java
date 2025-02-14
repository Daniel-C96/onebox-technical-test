package com.danielcontador.onebox_technical_test.service;

import com.danielcontador.onebox_technical_test.dto.ProductDto;
import com.danielcontador.onebox_technical_test.entity.Cart;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.UUID;

public interface CartService {
    Cart createCart();

    Cart getCart(UUID cartId);

    Cart deleteCart(UUID cartId);

    Cart addProduct(UUID cartId, ProductDto productDto);

    void deleteInactiveCarts();
}
