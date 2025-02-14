package com.danielcontador.onebox_technical_test.service;

import com.danielcontador.onebox_technical_test.entity.Cart;
import com.danielcontador.onebox_technical_test.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CartServiceImplTest {

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCart_ShouldReturnCart() {
        Cart cart = cartService.createCart();

        assertNotNull(cart);
        assertTrue(cart.getProducts().isEmpty());
    }

    @Test
    void getCart_ShouldReturnCartCreated() {
        Cart cart = cartService.createCart();
        Cart cartGotten = cartService.getCart(cart.getId());

        assertEquals(cart.getId(), cartGotten.getId());
        assertEquals(cart.getProducts(), cartGotten.getProducts());
        assertEquals(cart.getLastUpdate(), cartGotten.getLastUpdate());
    }

    @Test
    void getCart_ShouldThrowNoSuchElementException() {
        UUID nonExistentCartId = UUID.randomUUID();

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            cartService.getCart(nonExistentCartId);
        });
        assertEquals("Cart not found with ID: " + nonExistentCartId, exception.getMessage());
    }

    @Test
    void deleteCart() {
        Cart cart = cartService.createCart();
        assertNotNull(cartService.getCart(cart.getId()));

        cartService.deleteCart(cart.getId());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            cartService.getCart(cart.getId());
        });
        assertEquals("Cart not found with ID: " + cart.getId(), exception.getMessage());
    }

    @Test
    void addProducts_ShouldReturnTheCart() {
        Product product = new Product("Eggs", 2);
        product.setId(1L);

        when(productService.getProduct(1L)).thenReturn(product);
        System.out.println(productService.getProduct(1L));

        Cart cart = cartService.createCart();

        cartService.addProduct(cart.getId(), 1L);
        assertEquals("Eggs", cart.getProducts().getFirst().getDescription());
    }


    @Test
    void add_Products_ShouldThrowNoElementFoundException() {
        Product product = new Product("Eggs", 2);
        when(productService.getProduct(1)).thenReturn(product);

        UUID randomUUID = UUID.randomUUID();

        NoSuchElementException exceptionCart = assertThrows(NoSuchElementException.class, () -> {
            cartService.addProduct(randomUUID, 1);
        });
        assertEquals("Cart not found with ID: " + randomUUID, exceptionCart.getMessage());
    }

    @Test
    void deleteInactiveCarts_ShouldRemoveInactiveCarts() {
        Cart activeCart = cartService.createCart();
        activeCart.setLastUpdate(Instant.now().minusSeconds(300)); // 5 minutes

        Cart inactiveCart = cartService.createCart();
        inactiveCart.setLastUpdate(Instant.now().minusSeconds(1200)); // 20 minutes

        assertNotNull(activeCart);
        assertNotNull(inactiveCart);

        cartService.deleteInactiveCarts();

        assertNotNull(cartService.getCart(activeCart.getId()));
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            cartService.getCart(inactiveCart.getId());
        });
        assertEquals("Cart not found with ID: " + inactiveCart.getId(), exception.getMessage());
    }
}
