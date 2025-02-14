package com.danielcontador.onebox_technical_test.service;

import com.danielcontador.onebox_technical_test.dto.ProductDto;
import com.danielcontador.onebox_technical_test.entity.Cart;
import com.danielcontador.onebox_technical_test.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CartServiceImpl implements CartService {

    private final Map<UUID, Cart> carts = new ConcurrentHashMap<>();

    @Override
    public Cart createCart() {
        Cart cart = new Cart();
        carts.put(cart.getId(), cart);
        return cart;
    }

    @Override
    public Cart getCart(UUID cartId) {
        Cart cart = carts.get(cartId);
        if (cart == null) {
            throw new NoSuchElementException("Cart not found with ID: " + cartId);
        }
        return cart;
    }

    @Override
    public Cart deleteCart(UUID cartId) {
        Cart cart = carts.get(cartId);
        if (cart == null) {
            throw new NoSuchElementException("Cart not found with ID: " + cartId);
        }
        return carts.remove(cartId);
    }

    @Override
    public Cart addProduct(UUID cartId, ProductDto productDto) {
        Cart cart = carts.get(cartId);
        if (cart == null) {
            throw new NoSuchElementException("Cart not found with ID: " + cartId);
        }

        Product product = new Product(productDto.description(), productDto.amount());

        cart.getProducts().add(product);
        cart.setLastUpdate(Instant.now());
        return cart;
    }

    @Override
    @Scheduled(fixedRate = 15000) // Execute every 15 seconds
    public void deleteInactiveCarts() {
        Instant now = Instant.now();
        carts.entrySet().removeIf(entry -> {
            if (Duration.between(entry.getValue().getLastUpdate(), now).toMinutes() > 10) {
                System.out.println("The cart with ID " + entry.getKey() + " has been eliminated due to inactivity.");
                return true;
            }
            return false;
        });
    }
}
