package com.artyom.romashkako.product.controller;

import com.artyom.romashkako.common.exception.NotFoundException;
import com.artyom.romashkako.common.mapper.ValidationExceptionMapper;
import com.artyom.romashkako.product.dto.ProductRequest;
import com.artyom.romashkako.product.dto.ProductResponse;
import com.artyom.romashkako.product.mapper.ProductMapper;
import com.artyom.romashkako.product.model.Product;
import com.artyom.romashkako.product.service.DefaultProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductsController.class)
@Import({ProductMapper.class, ValidationExceptionMapper.class})
class ProductsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProductMapper mapper;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    DefaultProductService productService;

    Product product;

    ProductRequest request;

    ProductResponse response;
    @Autowired
    private ProductMapper productMapper;

    @Test
    void createNewProduct_returnCreatedResponseEntity_success() throws Exception {
        product = new Product(1, "Роза", "Красный цветок с шипами", BigDecimal.valueOf(34.99), true);
        request = new ProductRequest("Роза", "Красный цветок с шипами", BigDecimal.valueOf(34.99), true);
        response = new ProductResponse(1, "Роза", "Красный цветок с шипами", BigDecimal.valueOf(34.99).toPlainString(), true);

        doReturn(response).when(productService).create(any(ProductRequest.class));
        mockMvc.perform(post("/api/v1/product")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/api/v1/product?id=1"));
    }

    @Test
    void createNewProduct_whereProductNull_returnStatusBadRequest_success() throws Exception {
        mockMvc.perform(post("/api/v1/product")
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNewProduct_whereTitleIsNull_returnValidationException_success() throws Exception {
        request = new ProductRequest(null, "Красный цветок с шипами", BigDecimal.valueOf(34.99), true);

        doReturn(response).when(productService).create(any(ProductRequest.class));

        mockMvc.perform(post("/api/v1/product")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(HttpStatus.BAD_REQUEST.getReasonPhrase()));
    }

    @Test
    void createNewProduct_wherePriceNegativeValue_returnBadRequest_success() throws Exception {
        request = new ProductRequest("Роза", "Красный цветок с шипами", BigDecimal.valueOf(-34.99), true);

        doReturn(response).when(productService).create(any(ProductRequest.class));

        mockMvc.perform(post("/api/v1/product")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(HttpStatus.BAD_REQUEST.getReasonPhrase()));
    }

    @Test
    void updateProductById_returnChangedProduct_success() throws Exception {
        var id = 1;

        ProductResponse initResponse = new ProductResponse(1, "Роза", "Красный цветок c шипами", BigDecimal.valueOf(34.99).toPlainString(), true);

        ProductRequest updateRequest = new ProductRequest("Роза", "Красный цветок без шипов", BigDecimal.valueOf(34.99), true);
        ProductResponse updateResponse = new ProductResponse(id, "Роза", "Красный цветок без шипов", BigDecimal.valueOf(34.99).toPlainString(), true);

        doReturn(initResponse).when(productService).findById(id);
        doReturn(updateResponse).when(productService).updateById(any(ProductRequest.class), anyInt());

        mockMvc.perform(patch("/api/v1/product/{id}", id)
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Роза"))
                .andExpect(jsonPath("$.description").value("Красный цветок без шипов"))
                .andExpect(jsonPath("$.price").value(34.99))
                .andExpect(jsonPath("$.isAvailable").value(true));

        assertNotEquals(initResponse.description(), updateResponse.description());
    }

    @Test
    void fetchById_returnExistProduct_success() throws Exception {
        var id = 1;

        ProductResponse result = new ProductResponse(id, "Роза", "Красный цветок без шипов", BigDecimal.valueOf(34.99).toPlainString(), true);

        doReturn(result).when(productService).findById(id);

        mockMvc.perform(get("/api/v1/product/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Роза"))
                .andExpect(jsonPath("$.description").value("Красный цветок без шипов"))
                .andExpect(jsonPath("$.price").value(34.99))
                .andExpect(jsonPath("$.isAvailable").value(true));
    }

    @Test
    void fetchById_whereProductHasNotExist_returnNotFound_success() throws Exception {

        doThrow(new NotFoundException("Product by id not found")).when(productService).findById(Integer.MAX_VALUE);

        mockMvc.perform(get("/api/v1/product/{id}", Integer.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value("Product by id not found"));
    }

    @Test
    void fetchAll_returnAllProducts_success() throws Exception {
        var result = Stream.of(
                new Product(1, "Роза", "Красный цветок с шипами", BigDecimal.valueOf(34.99), true),
                new Product(2, "Тюльпан", "Весенний цветок различных оттенков", BigDecimal.valueOf(20.99), true),
                new Product(3, "Гвоздика", "Цветок с гофрированными лепестками", BigDecimal.valueOf(20.99), false),
                new Product(4, "Пионы", "Крупный цветок с пышными лепестками", BigDecimal.valueOf(29.99), true),
                new Product(5, "Лилии", "Элегантный цветок с сильным ароматом", BigDecimal.valueOf(25.99), false),
                new Product(6, "Ромашка", "Маленький белый цветок с желтой серединкой", BigDecimal.valueOf(5.99), true)
        ).map(productMapper::getProductResponse).toList();

        doReturn(result).when(productService).fetchAll();

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/product/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String jsonListResult = objectMapper.writeValueAsString(result);

        assertEquals(jsonListResult, jsonResponse);
    }

    @Test
    void fetchAll_whereListIsEmpty_returnEmptyList() throws Exception {
        var result = Collections.emptyList();

        doReturn(result).when(productService).fetchAll();

        MvcResult response = mockMvc.perform(get("/api/v1/product/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        String jsonListResult = objectMapper.writeValueAsString(result);

        assertEquals(jsonResponse, jsonListResult);
    }

    @Test
    void deleteById_whereProductExist_success() throws Exception {
        mockMvc.perform(delete("/api/v1/product/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    void deleteById_whereProductHasNotExist_success() throws Exception {
        mockMvc.perform(delete("/api/v1/product/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}