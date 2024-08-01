package com.artyom.romashkako.product.service;

import com.artyom.romashkako.product.data.ProductRepository;
import com.artyom.romashkako.product.data.SearchCriteriaProductRepository;
import com.artyom.romashkako.product.dto.ProductRequest;
import com.artyom.romashkako.product.dto.ProductResponse;
import com.artyom.romashkako.common.exception.NotFoundException;
import com.artyom.romashkako.product.mapper.ProductMapper;
import lombok.AllArgsConstructor;

import java.util.List;


@AllArgsConstructor
public class DefaultProductService implements ProductService {

    private final ProductRepository productRepository;
    private final SearchCriteriaProductRepository criteriaProductRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse create(ProductRequest request) {
        return productMapper.getProductResponse(productRepository.create(productMapper.getProduct(request)));
    }

    @Override
    public ProductResponse updateById(ProductRequest request, Integer id) {
        return productMapper.getProductResponse(productRepository.update(
                productMapper.mergeProduct(productRepository.findById(id).orElseThrow(() ->
                        new NotFoundException("Product by id " + id + " not found.")), request)
        ));
    }

    @Override
    public List<ProductResponse> fetchAll() {
        return productRepository.findAll().stream()
                .map(productMapper::getProductResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> findByCriteria(String title, Double priceGT, Double priceGL, Boolean available, Integer limit) {
        return criteriaProductRepository.findByCriteria(title, priceGT, priceGL, available, limit).stream()
                .map(productMapper::getProductResponse)
                .toList();
    }

    @Override
    public ProductResponse findById(Integer id) {
        return productRepository.findById(id)
                .map(productMapper::getProductResponse)
                .orElseThrow(() -> new NotFoundException("Product by id " + id + " not found."));
    }

    @Override
    public void deleteById(Integer id) {
        if (productRepository.deleteProductById(id) == 0) {
            throw new NotFoundException("Product by id " + id + " not found.");
        }
    }
}
