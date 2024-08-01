package com.artyom.romashkako.product.data;

import com.artyom.romashkako.product.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;

public interface SearchCriteriaProductRepository {
    List<Product> findByCriteria(String title, Double priceGT, Double priceLT, Boolean available, Integer limit);
}
