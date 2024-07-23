package com.artyom.romashkako.product.service;

import com.artyom.romashkako.product.data.InMemoryProductRepository;
import com.artyom.romashkako.product.dto.ProductRequest;
import com.artyom.romashkako.product.dto.ProductResponse;
import com.artyom.romashkako.common.exception.NotFoundException;
import com.artyom.romashkako.product.mapper.ProductMapper;
import com.artyom.romashkako.product.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final InMemoryProductRepository inMemoryProductRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse create(ProductRequest request) {
        return productMapper.getProductResponse(inMemoryProductRepository.save(productMapper.getProduct(request)));
    }

    @Override
    public ProductResponse updateById(ProductRequest request, Integer id) {
        Product product =   productMapper.mergeProduct(inMemoryProductRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product by id " + id + " not found.")), request);
        return productMapper.getProductResponse(inMemoryProductRepository.save(product));
    }

    @Override
    public List<ProductResponse> fetchAll() {
        return inMemoryProductRepository.findAll().stream()
                .map(productMapper::getProductResponse)
                .toList();
    }

    @Override
    public ProductResponse findById(Integer id) {
        return inMemoryProductRepository.findById(id)
                .map(productMapper::getProductResponse)
                .orElseThrow(() -> new NotFoundException("Product by id " + id + " not found."));
    }

    @Override
    public void deleteById(Integer id) {
        if (!inMemoryProductRepository.deleteById(id)) {
            throw new NotFoundException("Product by id " + id + " not found.");
        }
    }
}
