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
                                                  BindingResult bindingResult,
                                                  UriComponentsBuilder uriBuilder) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException e) {
                throw e;
            } else {
                throw new BindException(bindingResult);
            }
        }

        ProductResponse info = productService.create(request);
        return ResponseEntity
                .created(uriBuilder.replacePath("/api/v1/product?id={id}")
                        .build(Map.of("id", info.id())))
                .body(info);
    }

    @PutMapping("/edit")
    public ResponseEntity<ProductResponse> editProductById(@RequestParam Integer id,
                                                           @RequestBody @Valid ProductRequest request,
                                                           BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException e) {
                throw e;
            } else {
                throw new BindException(bindingResult);
            }
        }

        ProductResponse info = productService.updateById(request, id);
        return ResponseEntity.ok().body(info);
    }

    @GetMapping
    public ResponseEntity<ProductResponse> fetchProductById(@RequestParam(required = false) Integer id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping("/list")
    public List<ProductResponse> products() {
        return productService.fetchAll();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProductById(@RequestParam(required = false) Integer id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
