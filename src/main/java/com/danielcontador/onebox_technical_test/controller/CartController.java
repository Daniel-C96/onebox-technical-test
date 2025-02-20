package com.danielcontador.onebox_technical_test.controller;

import com.danielcontador.onebox_technical_test.dto.request.ProductDto;
import com.danielcontador.onebox_technical_test.dto.response.DeleteResponse;
import com.danielcontador.onebox_technical_test.entity.Cart;
import com.danielcontador.onebox_technical_test.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/carts")
@Tag(name = "Cart Controller", description = "Operations related to shopping carts, including creation, viewing, " +
        "modifying cart contents, and deletion.")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "Creates a new cart",
            description = "This endpoint creates a new, empty cart."
    )
    @PostMapping()
    private ResponseEntity<Cart> createCart() {
        Cart cartCreated = cartService.createCart();
        return ResponseEntity.status(HttpStatus.CREATED).body(cartCreated);
    }

    @Operation(
            summary = "Gets a cart by its ID",
            description = "This endpoint retrieves the cart with the given ID. You must provide a valid cart " +
                    "ID to fetch the details."
    )
    @GetMapping("/{id}")
    private Cart getCart(@Parameter(description = "ID of the cart to retrieve", required = true) @PathVariable UUID id) {
        return cartService.getCart(id);
    }

    @Operation(summary = "Adds a product to a cart",
            description = "This endpoint adds a product to an existing cart. You must provide the cart ID " +
                    "and a ProductDto in the body."
    )
    @PutMapping("/{cartId}")
    private Cart addProduct(
            @Parameter(description = "The ID of the cart to add the product to", required = true) @PathVariable UUID cartId,
            @Parameter(description = "The Product DTO", required = true) @Valid @RequestBody ProductDto productDto) {
        return cartService.addProduct(cartId, productDto);
    }

    @Operation(summary = "Deletes a cart by its ID",
            description = "This endpoint deletes a cart. You must provide the cart ID."
    )
    @DeleteMapping("/{cartId}")
    private DeleteResponse deleteCart(
            @Parameter(description = "The ID of the cart to delete", required = true) @PathVariable UUID cartId) {
        return cartService.deleteCart(cartId);
    }
}
