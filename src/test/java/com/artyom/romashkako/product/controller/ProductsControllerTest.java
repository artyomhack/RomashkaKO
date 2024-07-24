package com.artyom.romashkako.product.controller;

import com.artyom.romashkako.common.dto.ErrorResponse;
import com.artyom.romashkako.common.mapper.ValidationExceptionMapper;
import com.artyom.romashkako.product.data.InMemoryProductRepository;
import com.artyom.romashkako.product.data.ProductRepository;
import com.artyom.romashkako.product.dto.ProductRequest;
import com.artyom.romashkako.product.dto.ProductResponse;
import com.artyom.romashkako.product.mapper.ProductMapper;
import com.artyom.romashkako.product.service.DefaultProductService;
import com.artyom.romashkako.product.utils.ErrorResponseUtils;
import com.artyom.romashkako.product.utils.ProductUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.artyom.romashkako.product.utils.ErrorResponseUtils.*;

@WebMvcTest(controllers = ProductsController.class)
@Import({ProductMapper.class,
        ValidationExceptionMapper.class,
        InMemoryProductRepository.class,
        DefaultProductService.class,
        ProductUtils.class,
        ErrorResponseUtils.class})
class ProductsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ProductUtils productUtils;

    @Autowired
    ErrorResponseUtils errorResponseUtils;

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
        var expected = errorResponseUtils.getNotFound();
        var expectedId = Integer.MAX_VALUE;

        if (productRepository.findById(expectedId).isPresent())
            productRepository.deleteById(expectedId);

        var response = mockMvc
                .perform(delete("/api/v1/product/" + expectedId))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        var actual = objectMapper.readValue(response, ErrorResponse.class);

        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getStatus(), actual.getStatus());
    }

    @Test
    public void shouldCreateNewProduct_201created() throws Exception {
        var expected = productUtils.createRandomProduct("MyProduct");
        var request = productUtils.getProductRequest(expected);
        var response = mockMvc.perform(post("/api/v1/product")
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        var actual = objectMapper.readValue(response, ProductResponse.class);

        assertEquals(expected.getId(), actual.id());
        assertEquals(expected.getTitle(), actual.title());
        assertEquals(expected.getDescription(), actual.description());
        assertEquals(expected.getPrice(), new BigDecimal(actual.price()));
        assertEquals(expected.isAvailable(), actual.isAvailable());
    }

    @Test
    public void shouldCatchThrowWhenCreateIncorrectProduct_400badRequest() throws Exception {
        var expectedErrorBadRequest = errorResponseUtils.getBadRequest();
        var productTitleIsEmpty = productUtils.getRandomProduct("");
        var requestTitleIsEmpty = productUtils.getProductRequest(productTitleIsEmpty);

        var responseTitleIsNull = mockMvc.perform(post("/api/v1/product")
                        .content(objectMapper.writeValueAsString(requestTitleIsEmpty))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        var actualResponseErrorWhereTitleIsNull = objectMapper.readValue(responseTitleIsNull, ErrorResponse.class);

        assertEquals(expectedErrorBadRequest.getCode(), actualResponseErrorWhereTitleIsNull.getCode());
        assertEquals(expectedErrorBadRequest.getStatus(), actualResponseErrorWhereTitleIsNull.getStatus());
        assertEquals(TITLE_IS_EMPTY, actualResponseErrorWhereTitleIsNull.getErrors().get("title"));

        var productTitleIsMore255 = productUtils.getRandomProduct("""
                Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor.
                 Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.
                  Donec quam felis, ultricies nec, pellentesque eu, pretium quis,.
                """);
        var requestTitleIsMore255 = productUtils.getProductRequest(productTitleIsMore255);

        var responseTitleMore255 = mockMvc.perform(post("/api/v1/product")
                        .content(objectMapper.writeValueAsString(requestTitleIsMore255))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        var actualResponseErrorWhereTitleMore255 = objectMapper.readValue(responseTitleMore255, ErrorResponse.class);

        assertEquals(expectedErrorBadRequest.getCode(), actualResponseErrorWhereTitleMore255.getCode());
        assertEquals(expectedErrorBadRequest.getStatus(), actualResponseErrorWhereTitleMore255.getStatus());
        assertEquals(TITLE_MORE_255, actualResponseErrorWhereTitleMore255.getErrors().get("title"));

        var productDescriptionMore4096 = productUtils.getRandomProduct("MyProduct", TEXT_DESCRIPTION_MORE_4096, null, false);
        var requestDescriptionMore4096 = productUtils.getProductRequest(productDescriptionMore4096);

        var responseDescriptionMore4096 = mockMvc.perform(post("/api/v1/product")
                        .content(objectMapper.writeValueAsString(requestDescriptionMore4096))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        var actualResponseErrorWhereDescriptionMore4096 = objectMapper.readValue(responseDescriptionMore4096, ErrorResponse.class);

        assertEquals(expectedErrorBadRequest.getCode(), actualResponseErrorWhereDescriptionMore4096.getCode());
        assertEquals(expectedErrorBadRequest.getStatus(), actualResponseErrorWhereDescriptionMore4096.getStatus());
        assertEquals(DESCRIPTION_MORE_4096, actualResponseErrorWhereDescriptionMore4096.getErrors().get("description"));

        var productPriceLessZero = productUtils.getRandomProduct("MyProduct", "product info", BigDecimal.valueOf(-10), false);
        var requestPriceLessZero = productUtils.getProductRequest(productPriceLessZero);

        var responsePriceLessZero= mockMvc.perform(post("/api/v1/product")
                        .content(objectMapper.writeValueAsString(requestPriceLessZero))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        var actualResponseErrorWherePriceLessZero = objectMapper.readValue(responsePriceLessZero, ErrorResponse.class);

        assertEquals(expectedErrorBadRequest.getCode(), actualResponseErrorWherePriceLessZero.getCode());
        assertEquals(expectedErrorBadRequest.getStatus(), actualResponseErrorWherePriceLessZero.getStatus());
        assertEquals(PRICE_LESS_ZERO, actualResponseErrorWherePriceLessZero.getErrors().get("price"));
    }

}