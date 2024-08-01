package com.artyom.romashkako.product.controller;

import com.artyom.romashkako.product.dto.ProductResponse;
import com.artyom.romashkako.product.dto.ProductRequest;
import com.artyom.romashkako.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductsController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody @Valid ProductRequest request,
                                                  UriComponentsBuilder uriBuilder) {
        ProductResponse info = productService.create(request);
        return ResponseEntity
                .created(uriBuilder.replacePath("/api/v1/product")
                        .queryParam("id", info.id())
                        .build()
                        .toUri()
                )
                .body(info);
    }

    @PatchMapping("/{id}")
    public ProductResponse editProductById(@PathVariable Integer id,
                                           @RequestBody @Valid ProductRequest request) {
        return productService.updateById(request, id);
    }

    @GetMapping("/{id}")
    public ProductResponse fetchProductById(@PathVariable Integer id) {
        return productService.findById(id);
    }

    @GetMapping("/list")
    public List<ProductResponse> fetchAllProducts() {
        return productService.fetchAll();
    }

    @GetMapping("/search")
    public List<ProductResponse> searchProducts(@RequestParam(name = "title", required = false) String title,
                                                @RequestParam(name = "priceGt", required = false) Double priceGt,
                                                @RequestParam(name = "priceLt", required = false) Double priceLt,
                                                @RequestParam(name = "available", required = false, defaultValue = "true") Boolean available,
                                                @RequestParam(name = "limit", required = false) Integer limit,
                                                @RequestParam(name = "sortBy", required = false ) String sort) {

        return productService.findByCriteria(title, priceGt, priceLt, available, limit, sort);
    }

    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable Integer id) {
        productService.deleteById(id);
    }
}
