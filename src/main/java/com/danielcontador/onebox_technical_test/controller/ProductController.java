package com.danielcontador.onebox_technical_test.controller;

import com.danielcontador.onebox_technical_test.dto.ProductDto;
import com.danielcontador.onebox_technical_test.entity.Product;
import com.danielcontador.onebox_technical_test.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Product Controller", description = "Operations related to products.")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "Creates a new product",
            description = "This endpoint allows you to create a new product by providing a valid ProductDto."
    )
    @PostMapping("/create/product")
    private Product createProduct(@Valid @RequestBody ProductDto productDto) {
        return productService.createProduct(productDto);
    }
}
