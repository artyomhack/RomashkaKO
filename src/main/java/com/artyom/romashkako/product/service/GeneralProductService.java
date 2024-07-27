package com.artyom.romashkako.product.service;

import com.artyom.romashkako.product.data.ProductRepository;
import com.artyom.romashkako.product.dto.ProductRequest;
import com.artyom.romashkako.product.dto.ProductResponse;
import com.artyom.romashkako.product.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GeneralProductService implements ProductService {

    private final DefaultProductService productService;

    public GeneralProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productService = new DefaultProductService(productRepository, productMapper);
    }

    @Override
    @Transactional
    public ProductResponse create(ProductRequest request) {
        return productService.create(request);
    }

    @Override
    @Transactional
    public ProductResponse updateById(ProductRequest request, Integer id) {
        return productService.updateById(request, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> fetchAll() {
        return productService.fetchAll();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findById(Integer id) {
        return productService.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        productService.deleteById(id);
    }
}
