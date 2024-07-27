package com.artyom.romashkako.product.data;

import com.artyom.romashkako.product.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrudProductRepository extends CrudRepository<Product, Integer> {
}
