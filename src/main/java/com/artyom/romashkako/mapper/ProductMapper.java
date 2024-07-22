package com.artyom.romashkako.mapper;

import com.artyom.romashkako.dto.ProductRequest;
import com.artyom.romashkako.dto.ProductResponse;
import com.artyom.romashkako.model.Product;
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

    public ProductResponse getProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice().toPlainString(),
                product.isAvailable()
        );
    }

}
