package com.danielcontador.onebox_technical_test.controller;

import com.danielcontador.onebox_technical_test.dto.ProductDto;
import com.danielcontador.onebox_technical_test.entity.Cart;
import com.danielcontador.onebox_technical_test.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Cart Controller", description = "Operations related to shopping carts, including creation, viewing, and modifying cart contents.")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "Creates a new cart",
            description = "This endpoint allows you to create a new, empty cart."
    )
    @PostMapping("/create/cart")
    private Cart createCart() {
        return cartService.createCart();
    }

    @Operation(
            summary = "Gets a cart by its ID",
            description = "This endpoint retrieves the cart with the given ID. You must provide a valid cart ID to fetch the details."
    )
    @GetMapping("/carts/{id}")
    private Cart getCart(@Parameter(description = "ID of the cart to retrieve", required = true) @PathVariable UUID id) {
        return cartService.getCart(id);
    }

    @Operation(summary = "Add a product to the cart",
            description = "This endpoint allows you to add a product to an existing cart. You must provide the cart ID and the product ID."
    )
    @PostMapping("/add/{cartId}")
    private Cart addProduct(
            @Parameter(description = "The ID of the cart to add the product to", required = true) @PathVariable UUID cartId,
            @Parameter(description = "The Product DTO", required = true) @RequestBody ProductDto productDto) {
        return cartService.addProduct(cartId, productDto);
    }

    @Operation(summary = "Deletes a cart by its ID",
            description = "This endpoint allows you to delete a cart. You must provide the cart ID."
    )
    @DeleteMapping("/delete/cart/{cartId}")
    private Cart deleteCart(
            @Parameter(description = "The ID of the cart to delete", required = true) @PathVariable UUID cartId) {
        return cartService.deleteCart(cartId);
    }
}
