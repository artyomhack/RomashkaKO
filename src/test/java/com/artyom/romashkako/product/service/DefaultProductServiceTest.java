package com.artyom.romashkako.product.service;

import com.artyom.romashkako.common.exception.NotFoundException;
import com.artyom.romashkako.product.data.InMemoryProductRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
public class DefaultProductServiceTest {

    private final ProductUtils productUtils = new ProductUtils();

    @Mock
    private InMemoryProductRepository inMemoryProductRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private DefaultProductService defaultProductService;

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

        when(inMemoryProductRepository.findById(1)).thenReturn(Optional.empty());
        when(inMemoryProductRepository.save(product)).thenReturn(product);
        when(productMapper.getProduct(request)).thenReturn(product);
        when(productMapper.getProductResponse(product)).thenReturn(expectedResponse);

        var result = defaultProductService.create(request);

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

        when(inMemoryProductRepository.findById(1)).thenReturn(Optional.of(product));
        when(inMemoryProductRepository.save(updateProduct)).thenReturn(updateProduct);
        when(productMapper.getProduct(request)).thenReturn(updateProduct);
        when(productMapper.mergeProduct(product, request)).thenReturn(updateProduct);
        when(productMapper.getProductResponse(updateProduct)).thenReturn(expectedResponse);

        var result = defaultProductService.updateById(request, id);

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

        when(inMemoryProductRepository.findById(product.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> defaultProductService.updateById(request,product.getId()));
    }

    //findById -> fetch, not found
    //findAll -> fetchAll
    //deleteById -> delete, not found
}