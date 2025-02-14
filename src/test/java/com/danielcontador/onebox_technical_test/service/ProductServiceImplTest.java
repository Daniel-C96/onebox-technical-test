package com.danielcontador.onebox_technical_test.service;

import com.danielcontador.onebox_technical_test.dto.ProductDto;
import com.danielcontador.onebox_technical_test.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProduct_ShouldReturnProduct() {
        ProductDto productDto = new ProductDto("Eggs", 2);
        Product result = productService.createProduct(productDto);

        assertNotNull(result);
        assertEquals("Eggs", result.getDescription());
        assertEquals(2, result.getAmount());
    }

    @Test
    void getProduct_ShouldReturnProduct() {
        ProductDto productDto = new ProductDto("Eggs", 2);
        Product createdProduct = productService.createProduct(productDto);

        Product result = productService.getProduct(createdProduct.getId());

        assertNotNull(result);
        assertEquals(createdProduct.getId(), result.getId());
        assertEquals("Eggs", result.getDescription());
        assertEquals(2, result.getAmount());
    }

    @Test
    void getProduct_ShouldThrowNoSuchElementException() {
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            productService.getProduct(999);
        });

        assertEquals("Product not found with ID: 999", exception.getMessage());
    }
}

