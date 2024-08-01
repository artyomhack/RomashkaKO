package com.artyom.romashkako.product.service;

import com.artyom.romashkako.product.data.ProductRepository;
import com.artyom.romashkako.product.data.SearchCriteriaProductRepository;
import com.artyom.romashkako.product.dto.ProductRequest;
import com.artyom.romashkako.product.dto.ProductResponse;
import com.artyom.romashkako.product.mapper.ProductMapper;
import com.artyom.romashkako.product.mapper.TypeSortMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionalProductRepository extends DefaultProductService {

    public TransactionalProductRepository(ProductRepository productRepository, SearchCriteriaProductRepository criteriaProductRepository,
                                          ProductMapper productMapper, TypeSortMapper sortMapper) {
        super(productRepository, criteriaProductRepository, productMapper, sortMapper);
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
    @Transactional(readOnly = true)
    public List<ProductResponse> findByCriteria(String title, Double priceGt, Double priceLt, Boolean available, Integer limit, String sort) {
        return super.findByCriteria(title, priceGt, priceLt, available, limit, sort);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        super.deleteById(id);
    }

}
