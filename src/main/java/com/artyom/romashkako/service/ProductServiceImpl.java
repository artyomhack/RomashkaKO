package com.artyom.romashkako.service;

import com.artyom.romashkako.mapper.ProductMapper;
import com.artyom.romashkako.model.Product;
import com.artyom.romashkako.data.repository.InMemoryProductRepository;
import com.artyom.romashkako.dto.ProductResponse;
import com.artyom.romashkako.dto.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final InMemoryProductRepository inMemoryProductRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse create(ProductRequest request) {
        Product product = inMemoryProductRepository.save(productMapper.getProduct(request));
        return productMapper.response(product);
    }

    @Override
    public ProductResponse updateById(ProductRequest request, Integer id) {
        Product product = inMemoryProductRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product by id " + id + " not found."));

        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setAvailable(request.isAvailable());

        return productMapper.response(inMemoryProductRepository.save(product));
    }

    @Override
    public List<ProductResponse> fetchAll() {
        return StreamSupport.stream(inMemoryProductRepository.findAll().spliterator(), false)
                .map(productMapper::response)
                .toList();
    }

    @Override
    public ProductResponse findById(Integer id) {
        Product product = inMemoryProductRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product by id " + id + " not found."));
        return productMapper.response(product);
    }

    @Override
    public void deleteById(Integer id) {
        inMemoryProductRepository.deleteById(id);
    }
}
