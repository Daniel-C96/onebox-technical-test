package com.danielcontador.onebox_technical_test.controller;

import com.danielcontador.onebox_technical_test.entity.Cart;
import com.danielcontador.onebox_technical_test.entity.Product;
import com.danielcontador.onebox_technical_test.service.CartService;
import com.danielcontador.onebox_technical_test.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private CartController cartController;
    @MockitoBean
    private CartService cartService;
    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCart_ShouldReturnCreatedCart() throws Exception {
        Cart cart = new Cart();
        when(cartService.createCart()).thenReturn(cart);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/create/cart"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(cart.getId().toString()));

    }

    @Test
    void getCart_ShouldReturnCartById() throws Exception {
        Cart cart = new Cart();
        when(cartService.getCart(any(UUID.class))).thenReturn(cart);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/carts/{id}", cart.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(cart.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("products").isEmpty());
    }

    @Test
    void getCart_ShouldReturnStatus404() throws Exception {
        UUID randomUUID = UUID.randomUUID();
        when(cartService.getCart(randomUUID)).thenThrow(new NoSuchElementException("Cart not found with ID: " + randomUUID));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/carts/{id}", randomUUID))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void addProduct_ShouldReturnCartWithProduct() throws Exception {
        Cart cart = new Cart();
        Product product = new Product("Eggs", 2);

        when(cartService.getCart(cart.getId())).thenReturn(cart);
        when(productService.getProduct(product.getId())).thenReturn(product);

        cart.getProducts().add(product);

        when(cartService.addProduct(cart.getId(), 1L)).thenReturn(cart);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/add/{cartId}/{productId}", cart.getId(), 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(cart.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("products[0].description").value("Eggs"))
                .andExpect(MockMvcResultMatchers.jsonPath("products[0].amount").value(2));
    }

    @Test
    void deleteCart_ShouldReturnDeletedCart() throws Exception {
        Cart cart = new Cart();
        when(cartService.deleteCart(cart.getId())).thenReturn(cart);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/delete/cart/{cartId}", cart.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(cart.getId().toString()));
    }

    @Test
    void deleteCart_ShouldThrowElementNotFoundException() throws Exception {
        when(cartService.deleteCart(any(UUID.class))).thenThrow(new NoSuchElementException());
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/delete/cart/{cartId}", UUID.randomUUID()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
