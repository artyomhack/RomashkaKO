package com.artyom.romashkako.product.controller;

import com.artyom.romashkako.product.dto.ProductResponse;
import com.artyom.romashkako.product.dto.ProductRequest;
import com.artyom.romashkako.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody @Valid ProductRequest request,
                                                  UriComponentsBuilder uriBuilder) {
        ProductResponse info = productService.create(request);
        return ResponseEntity
                .created(uriBuilder.replacePath("/api/v1/product?id={id}")
                        .build(Map.of("id", info.id())))
                .body(info);
    }

    @PutMapping("/edit/{id}")
    public ProductResponse editProductById(@PathVariable Integer id,
                                           @RequestBody @Valid ProductRequest request) {
        return productService.updateById(request, id);
    }

    @GetMapping("/{id}")
    public ProductResponse fetchProductById(@PathVariable Integer id) {
        return productService.findById(id);
    }

    @GetMapping("/list")
    public List<ProductResponse> products() {
        return productService.fetchAll();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProductById(@PathVariable Integer id) {
        productService.deleteById(id);
    }
}
