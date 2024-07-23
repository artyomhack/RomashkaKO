package com.artyom.romashkako.product.controller;

import com.artyom.romashkako.common.controller.AdviceController;
import com.artyom.romashkako.common.exception.ValidationException;
import com.artyom.romashkako.common.mapper.ValidationExceptionMapper;
import com.artyom.romashkako.product.dto.ProductRequest;
import com.artyom.romashkako.product.dto.ProductResponse;
import com.artyom.romashkako.product.mapper.ProductMapper;
import com.artyom.romashkako.product.model.Product;
import com.artyom.romashkako.product.service.DefaultProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductsController.class)
@Import({ProductMapper.class, ValidationExceptionMapper.class})
class ProductsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProductMapper mapper;

    @MockBean
    DefaultProductService productService;

    Product product;

    ProductRequest request;

    ProductResponse response;

    @BeforeEach
    public void setUp() {
        product = new Product(1, "Роза", "Красный цветок с шипами", BigDecimal.valueOf(34.99), true);
        request = new ProductRequest("Роза", "Красный цветок с шипами", BigDecimal.valueOf(34.99), true);
        response = new ProductResponse(1,"Роза", "Красный цветок с шипами", BigDecimal.valueOf(34.99).toPlainString(), true);
    }

    @Test
    void createNewProduct_ReturnCreatedResponseEntity() throws Exception {
        doReturn(response).when(productService).create(any(ProductRequest.class));
        mockMvc.perform(post("/api/v1/product")
                .content(asJsonString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/api/v1/product?id=1"));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}