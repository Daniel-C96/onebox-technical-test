package com.danielcontador.onebox_technical_test.controller;

import com.danielcontador.onebox_technical_test.dto.ProductDto;
import com.danielcontador.onebox_technical_test.entity.Product;
import com.danielcontador.onebox_technical_test.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private ProductController productController;

    @MockitoBean
    private ProductService productService;

    @Test
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        ProductDto productDto = new ProductDto("Eggs", 2);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(productDto);

        when(productService.createProduct(productDto)).thenReturn(new Product("Eggs", 2));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/create/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("description").value("Eggs"))
                .andExpect(MockMvcResultMatchers.jsonPath("amount").value(2));
    }

    @Test
    void createProduct_ShouldReturnBadRequest() throws Exception {
        ProductDto invalidProductDto = new ProductDto("", -1);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(invalidProductDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/create/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
