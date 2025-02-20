package com.danielcontador.onebox_technical_test.controller;

import com.danielcontador.onebox_technical_test.dto.request.ProductDto;
import com.danielcontador.onebox_technical_test.dto.response.DeleteResponse;
import com.danielcontador.onebox_technical_test.entity.Cart;
import com.danielcontador.onebox_technical_test.entity.Product;
import com.danielcontador.onebox_technical_test.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
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

    @MockitoBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCart_ShouldReturnCreatedCart() throws Exception {
        Cart cart = new Cart();
        when(cartService.createCart()).thenReturn(cart);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/carts"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
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

        ProductDto productDto = new ProductDto("Eggs", 2);
        String jsonRequest = objectMapper.writeValueAsString(productDto);

        Product product = new Product("Eggs", 2);

        when(cartService.getCart(cart.getId())).thenReturn(cart);
        cart.getProducts().add(product);

        when(cartService.addProduct(cart.getId(), productDto)).thenReturn(cart);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/carts/{cartId}", cart.getId(), product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(cart.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("products[0].description").value("Eggs"))
                .andExpect(MockMvcResultMatchers.jsonPath("products[0].amount").value(2));
    }

    @Test
    void deleteCart_ShouldReturnDeleteResponse() throws Exception {
        Cart cart = new Cart();
        when(cartService.deleteCart(cart.getId())).thenReturn(new DeleteResponse("terminated", "the cart has been deleted"));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/carts/{cartId}", cart.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("status").value("terminated"));
    }

    @Test
    void deleteCart_ShouldThrowElementNotFoundException() throws Exception {
        when(cartService.deleteCart(any(UUID.class))).thenThrow(new NoSuchElementException());
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/carts/{cartId}", UUID.randomUUID()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
