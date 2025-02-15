package com.danielcontador.onebox_technical_test.service;

import com.danielcontador.onebox_technical_test.dto.ProductDto;
import com.danielcontador.onebox_technical_test.entity.Cart;
import com.danielcontador.onebox_technical_test.entity.Product;
import com.danielcontador.onebox_technical_test.response.DeleteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CartServiceImpl implements CartService {

    private final Map<UUID, Cart> carts = new ConcurrentHashMap<>();
    private final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    private static final String CART_NOT_FOUND = "Cart not found with ID: ";

    @Override
    public Cart createCart() {
        Cart cart = new Cart();
        carts.put(cart.getId(), cart);
        logger.info("New cart created with ID: {}", cart.getId());
        return cart;
    }

    @Override
    public Cart getCart(UUID cartId) {
        return Optional.ofNullable(carts.get(cartId))
                .orElseThrow(() -> new NoSuchElementException(CART_NOT_FOUND + cartId));
    }

    @Override
    public DeleteResponse deleteCart(UUID cartId) {
        Optional.ofNullable(carts.remove(cartId))
                .orElseThrow(() -> new NoSuchElementException(CART_NOT_FOUND + cartId));

        logger.info("The cart with ID: {} has been deleted", cartId);
        return new DeleteResponse("terminated", "the cart has been deleted");
    }

    @Override
    public Cart addProduct(UUID cartId, ProductDto productDto) {
        Cart cart = Optional.ofNullable(carts.get(cartId))
                .orElseThrow(() -> new NoSuchElementException(CART_NOT_FOUND + cartId));

        Product product = new Product(productDto.description(), productDto.amount());
        cart.getProducts().add(product);
        cart.updateLastModified();

        logger.info("Product '{}' added to cart {}", productDto.description(), cartId);
        return cart;
    }

    @Override
    @Scheduled(fixedRate = 15000) // Executes every 15 seconds
    public void deleteInactiveCarts() {
        Instant expirationTime = Instant.now().minusSeconds(600);

        carts.entrySet().removeIf(entry -> {
            boolean isExpired = entry.getValue().getLastUpdate().isBefore(expirationTime);
            if (isExpired) {
                logger.info("The cart with ID {} has been removed due to inactivity.", entry.getKey());
            }
            return isExpired;
        });
    }
}
