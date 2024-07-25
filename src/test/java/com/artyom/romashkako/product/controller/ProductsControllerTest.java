package com.artyom.romashkako.product.controller;

import com.artyom.romashkako.StringUtils;
import com.artyom.romashkako.common.dto.ErrorResponse;
import com.artyom.romashkako.common.exception.NotFoundException;
import com.artyom.romashkako.common.mapper.ValidationExceptionMapper;
import com.artyom.romashkako.product.data.InMemoryProductRepository;
import com.artyom.romashkako.product.data.ProductRepository;
import com.artyom.romashkako.product.dto.ProductRequest;
import com.artyom.romashkako.product.dto.ProductResponse;
import com.artyom.romashkako.product.mapper.ProductMapper;
import com.artyom.romashkako.product.model.Product;
import com.artyom.romashkako.product.service.DefaultProductService;
import com.artyom.romashkako.product.service.ProductService;
import com.artyom.romashkako.product.utils.ProductUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductsController.class)
@Import({ProductMapper.class,
        ValidationExceptionMapper.class,
        InMemoryProductRepository.class,
        DefaultProductService.class,
        StringUtils.class,
        ProductUtils.class})
class ProductsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    StringUtils stringUtils;

    @Autowired
    ProductUtils productUtils;
    @Autowired
    private ProductMapper productMapper;

    @Test
    public void shouldReturnProductById_200ok() throws Exception {
        var expected = productUtils.createRandomProduct();
        var response = mockMvc
                .perform(get("/api/v1/product/" + expected.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        var actual = objectMapper.readValue(response, ProductResponse.class);

        assertEquals(expected.getId(), actual.id());
        assertEquals(expected.getTitle(), actual.title());
        assertEquals(expected.getDescription(), actual.description());
        assertEquals(expected.isAvailable(), actual.isAvailable());
    }

    @Test
    public void shouldCatchThrowWhenReturnMissingProductById_404notFound() throws Exception {
        var expectedId = Integer.MAX_VALUE;

        if (productRepository.findById(expectedId).isPresent())
            productRepository.deleteById(expectedId);

        var response = mockMvc
                .perform(delete("/api/v1/product/" + expectedId))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        var actual = objectMapper.readValue(response, ErrorResponse.class);

        assertEquals(HttpStatus.NOT_FOUND.name(), actual.getStatus());
        assertEquals(HttpStatus.NOT_FOUND.value(), actual.getCode());
    }

    @Test
    public void shouldCreateNewProduct_201created() throws Exception {
        var expected = productUtils.createRandomProduct();
        var request = productUtils.getProductRequest(expected);

        var response = mockMvc.perform(post("/api/v1/product")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());

        var actual = productUtils.readJson(response, ProductResponse.class);

        assertNotNull(expected.getId());
        assertEquals(expected.getTitle(), actual.title());
        assertEquals(expected.getDescription(), actual.description());
        assertEquals(expected.getPrice(), Double.parseDouble(actual.price()));
        assertEquals(expected.isAvailable(), actual.isAvailable());
    }

    @Test
    public void shouldCatchThrowWhenCreateIncorrectProduct_400badRequest() throws Exception {
        var request = new ProductRequest(
                stringUtils.getRandomString(300),
                stringUtils.getRandomString(4100),
                -100.00,
                false
        );

        var response = mockMvc.perform(post("/api/v1/product")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());

        var errorResp = productUtils.readJson(response, ErrorResponse.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.name(), errorResp.getStatus());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), errorResp.getCode());
        assertTrue(errorResp.getErrors().containsKey("title"));
        assertTrue(errorResp.getErrors().containsKey("description"));
        assertTrue(errorResp.getErrors().containsKey("price"));
    }

    @Test
    public void shouldReturnedUpdatedProductById_200ok() throws Exception {
        var oldProduct = productUtils.createRandomProduct();
        var expectedId = oldProduct.getId();
        var expectedTitle = "updated_" + oldProduct.getTitle();
        var expectedDescription = "updated_" + oldProduct.getDescription();

        var request = new ProductRequest(
                expectedTitle,
                expectedDescription,
                Double.MAX_VALUE,
                true
        );

        var response = mockMvc.perform(patch("/api/v1/product/" + expectedId)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        var actual = productUtils.readJson(response, ProductResponse.class);

        assertEquals(expectedId, actual.id());
        assertEquals(expectedTitle, actual.title());
        assertEquals(expectedDescription, actual.description());
        assertEquals(Double.MAX_VALUE, Double.valueOf(actual.price()));
        assertTrue(actual.isAvailable());
    }

    @Test
    public void shouldCatchThrowWhenNotFoundUpdatedProductById_404notFound() throws Exception {
        var oldProduct = productUtils.createRandomProduct();
        var expectedId = oldProduct.getId();
        var expectedTitle = stringUtils.getRandomString(15);
        var expectedDescription = stringUtils.getRandomString(115);
        var expectedPrice = 20.99;

        var request = new ProductRequest(expectedTitle, expectedDescription, expectedPrice, false);

        productRepository.deleteById(expectedId);

        var response = mockMvc.perform(patch("/api/v1/product/" + expectedId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());

        var errorResponse = productUtils.readJson(response, ErrorResponse.class);

        assertEquals(HttpStatus.NOT_FOUND.name(), errorResponse.getStatus());
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getCode());
    }


    @Test
    public void shouldCatchThrowWhenIncorrectUpdatedProductById_400badRequest() throws Exception {
        var oldProduct = productUtils.createRandomProduct();
        var expectedId = oldProduct.getId();
        var expectedTitle = stringUtils.getRandomString(300);
        var expectedDescription = stringUtils.getRandomString(4100);
        var expectedPrice = -1.0;

        var request = new ProductRequest(expectedTitle, expectedDescription, expectedPrice, false);

        var response = mockMvc.perform(patch("/api/v1/product/" + expectedId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());

        var errorResponse = productUtils.readJson(response, ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST.name(), errorResponse.getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getCode());
        assertTrue(errorResponse.getErrors().containsKey("title"));
        assertTrue(errorResponse.getErrors().containsKey("description"));
        assertTrue(errorResponse.getErrors().containsKey("price"));
    }

    @Test
    public void shouldDeleteProductById_200ok() throws Exception {
        var product = productUtils.createRandomProduct();

        mockMvc.perform(delete("/api/v1/product/" + product.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        assertThrows(NotFoundException.class, () -> productService.findById(product.getId()));
    }

    @Test
    public void shouldCatchThrowWhenDeleteNotExistProductById_404notFound() throws Exception {
        var id = Integer.MAX_VALUE;

        if (productRepository.findById(id).isPresent()) {
            productRepository.deleteById(id);
        }

        var response = mockMvc.perform(delete("/api/v1/product/" + id))
                .andDo(print())
                .andExpect(status().isNotFound());

        var errorResponse = productUtils.readJson(response, ErrorResponse.class);

        assertEquals(HttpStatus.NOT_FOUND.name(), errorResponse.getStatus());
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getCode());
    }

    @Test
    public void shouldReturnAllProducts_200ok() throws Exception {
        var expectedProducts = new ArrayList<>(List.of(
                new Product(1, "Роза", "Красный цветок с шипами", 34.99, true),
                new Product(2, "Тюльпан", "Весенний цветок различных оттенков", 20.99, true),
                new Product(3, "Гвоздика", "Цветок с гофрированными лепестками", 20.99, false),
                new Product(4, "Пионы", "Крупный цветок с пышными лепестками", 29.99, true),
                new Product(5, "Лилии", "Элегантный цветок с сильным ароматом", 25.99, false),
                new Product(6, "Ромашка", "Маленький белый цветок с желтой серединкой", 5.99, true)
        )).stream().map(it -> productMapper.getProductResponse(productRepository.save(it))).toList();

        var responses = mockMvc.perform(get("/api/v1/product/list"))
                .andDo(print())
                .andExpect(status().isOk());

        var actualList = Arrays.stream(productUtils.readJson(responses, ProductResponse[].class)).toList();

        assertFalse(actualList.isEmpty());
        assertThat(actualList).containsSequence(expectedProducts);
    }


    // create fun - Y
    // update 200,404, 400
    // delete 404, 200
    // fetch all -> create 10 item -> map to dto -> return list all 10 exist in fetchAll
    // ListAssert.assertThatDoubleStream() -> есть метод

}