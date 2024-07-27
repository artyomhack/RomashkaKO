package com.artyom.romashkako.product.mapper;

import com.artyom.romashkako.product.dto.ProductRequest;
import com.artyom.romashkako.product.dto.ProductResponse;
import com.artyom.romashkako.product.model.Product;
import org.springframework.stereotype.Component;


@Component
public class ProductMapper {

    public Product getProduct(ProductRequest request) {
        return new Product(
                null,
                request.getTitle(),
                request.getDescription(),
                request.getPrice(),
                request.isAvailable()
        );
    }

    public Product getProduct(ProductResponse response) {
        return new Product(
                response.id(),
                response.title(),
                response.description(),
                Double.parseDouble(response.price()),
                response.isAvailable()
        );
    }

    public ProductResponse getProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                String.valueOf(product.getPrice()),
                product.isAvailable()
        );
    }

    public Product mergeProduct(Product product, ProductRequest request) {
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setAvailable(request.isAvailable());
        return product;
    }
}
