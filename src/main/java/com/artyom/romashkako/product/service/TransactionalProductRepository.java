package com.artyom.romashkako.product.service;

import com.artyom.romashkako.product.data.ProductRepository;
import com.artyom.romashkako.product.dto.ProductRequest;
import com.artyom.romashkako.product.dto.ProductResponse;
import com.artyom.romashkako.product.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionalProductRepository extends DefaultProductService {

    public TransactionalProductRepository(ProductRepository productRepository, ProductMapper productMapper) {
        super(productRepository, productMapper);
    }

    @Override
    @Transactional
    public ProductResponse create(ProductRequest request) {
        return super.create(request);
    }

    @Override
    @Transactional
    public ProductResponse updateById(ProductRequest request, Integer id) {
        return super.updateById(request, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> fetchAll() {
        return super.fetchAll();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findById(Integer id) {
        return super.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        super.deleteById(id);
    }
}
