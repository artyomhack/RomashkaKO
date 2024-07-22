package com.artyom.romashkako.controller;

import com.artyom.romashkako.dto.ProductResponse;
import com.artyom.romashkako.dto.ProductRequest;
import com.artyom.romashkako.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
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

    @PutMapping("/edit")
    public ProductResponse editProductById(@RequestParam Integer id,
                                           @RequestBody @Valid ProductRequest request) {
        return productService.updateById(request, id);
    }

    @GetMapping
    public ProductResponse fetchProductById(@RequestParam(required = false) Integer id) {
        return productService.findById(id);
    }

    @GetMapping("/list")
    public List<ProductResponse> products() {
        return productService.fetchAll();
    }

    @DeleteMapping
    public void deleteProductById(@RequestParam(required = false) Integer id) {
        productService.deleteById(id);
    }
}
