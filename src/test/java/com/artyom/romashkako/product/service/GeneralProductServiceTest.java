package com.artyom.romashkako.product.service;

import com.artyom.romashkako.common.exception.NotFoundException;
import com.artyom.romashkako.product.data.InDataBaseProductRepository;
import com.artyom.romashkako.product.dto.ProductRequest;
import com.artyom.romashkako.product.dto.ProductResponse;
import com.artyom.romashkako.product.mapper.ProductMapper;
import com.artyom.romashkako.product.model.Product;
import com.artyom.romashkako.product.utils.ProductUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GeneralProductServiceTest {

    private final ProductUtils productUtils = new ProductUtils();

    @Mock
    private InDataBaseProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private GeneralProductService generalProductService;

    private Product product;

    @BeforeEach
    public void setUp() {
        product = productUtils.getRandomProduct(1);
    }

    @Test
    public void shouldReturnCreateNewProduct() {
        var request = new ProductRequest(product.getTitle(), product.getDescription(), product.getPrice(), product.isAvailable());
        var expectedResponse = new ProductResponse(product.getId(), product.getTitle(), product.getDescription(),
                String.valueOf(product.getPrice()), product.isAvailable());

        when(productRepository.findById(1)).thenReturn(Optional.empty());
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.getProduct(request)).thenReturn(product);
        when(productMapper.getProductResponse(product)).thenReturn(expectedResponse);

        var result = generalProductService.create(request);

        assertEquals(expectedResponse.id(), result.id());
        assertEquals(expectedResponse.title(), result.title());
        assertEquals(expectedResponse.description(), result.description());
        assertEquals(expectedResponse.price(), result.price());
        assertEquals(expectedResponse.isAvailable(), result.isAvailable());
    }

    @Test
    public void shouldReturnUpdatedProduct() {
        var updateProduct = new Product(
                product.getId(),
                "updated_" + product.getTitle(),
                "updated_" + product.getDescription(),
                200.99,
                true
        );

        var id = updateProduct.getId();
        var updatedTitle = updateProduct.getTitle();
        var updatedDescription = updateProduct.getDescription();
        var updatedPrice = updateProduct.getPrice();
        var updatedAvailable = updateProduct.isAvailable();

        var request = productUtils.getProductRequest(updateProduct);
        var expectedResponse = productUtils.getProductResponse(updateProduct);

        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(updateProduct)).thenReturn(updateProduct);
        when(productMapper.getProduct(request)).thenReturn(updateProduct);
        when(productMapper.mergeProduct(product, request)).thenReturn(updateProduct);
        when(productMapper.getProductResponse(updateProduct)).thenReturn(expectedResponse);

        var result = generalProductService.updateById(request, id);

        assertEquals(id, result.id());
        assertEquals(updatedTitle, result.title() );
        assertEquals(updatedDescription, result.description());
        assertEquals(updatedPrice, Double.parseDouble(result.price()));
        assertEquals(updatedAvailable, result.isAvailable());
    }

    @Test
    public void shouldCatchThrowWhenUpdatingNotExistProductById() {
        var updateProduct = new Product(
                product.getId(),
                "updated_" + product.getTitle(),
                "updated_" + product.getDescription(),
                200.99,
                true
        );

        var request = productUtils.getProductRequest(updateProduct);

        when(productRepository.findById(product.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> generalProductService.updateById(request,product.getId()));
    }

    @Test
    public void shouldReturnExistingProductById() {
        var id = product.getId();
        var expectedResponse = productUtils.getProductResponse(product);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productMapper.getProductResponse(product)).thenReturn(expectedResponse);

        var actual = generalProductService.findById(id);

        assertNotNull(actual);
        assertEquals(expectedResponse.id(), actual.id());
        assertEquals(expectedResponse.title(), actual.title());
        assertEquals(expectedResponse.description(), actual.description());
        assertEquals(expectedResponse.price(), actual.price());
        assertEquals(expectedResponse.isAvailable(), actual.isAvailable());
    }

    @Test
    public void shouldReturnHasNotExistProductById() {
        var id = product.getId();

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> generalProductService.findById(id));
    }

    @Test
    public void shouldReturnAllProducts() {
        var products = List.of(
                new Product(1, "Роза", "Красный цветок с шипами", 34.99, true),
                new Product(2, "Тюльпан", "Весенний цветок различных оттенков", 20.99, true),
                new Product(3, "Гвоздика", "Цветок с гофрированными лепестками", 20.99, false),
                new Product(4, "Пионы", "Крупный цветок с пышными лепестками", 29.99, true),
                new Product(5, "Лилии", "Элегантный цветок с сильным ароматом", 25.99, false),
                new Product(6, "Ромашка", "Маленький белый цветок с желтой серединкой", 5.99, true)
        );

        var expectedList = products.stream().map(it -> {
            var response = productUtils.getProductResponse(it);
            when(productMapper.getProductResponse(it)).thenReturn(response);
            return response;
        }).toList();

        when(productRepository.findAll()).thenReturn(products);

        var actualList = generalProductService.fetchAll();

        assertThat(actualList).containsSequence(expectedList);
    }

    @Test
    public void shouldDeleteExistProductById() {
        var id = product.getId();
        var expectedResponse = productUtils.getProductResponse(product);

        when(productRepository.deleteById(id)).thenReturn(true);
        when(productMapper.getProductResponse(product)).thenReturn(expectedResponse);

        generalProductService.deleteById(product.getId());

        verify(productRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void shouldDeleteHasNotExistProductById() {
        var id = product.getId();

        when(productRepository.findById(id)).thenReturn(Optional.empty());
        when(productRepository.deleteById(id)).thenReturn(false);

        assertEquals(Optional.empty(), productRepository.findById(id));

        assertThrows(NotFoundException.class, () -> generalProductService.deleteById(id));
    }

}