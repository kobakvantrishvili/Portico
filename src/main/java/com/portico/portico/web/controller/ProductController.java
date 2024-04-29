package com.portico.portico.web.controller;

import com.portico.portico.application.ProductService;
import com.portico.portico.domain.Product;
import com.portico.portico.domain.ProductStorage;
import com.portico.portico.mapper.ProductMapper;
import com.portico.portico.web.schema.ProductSchema;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("ALL")
@RestController
@RequestMapping("/product")
public class ProductController {
    final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(value = "/add")
    public CompletableFuture<ResponseEntity<Void>> addProduct(@RequestBody ProductSchema productSchema) {
        Product product = ProductMapper.mapToProduct(productSchema);
        ProductStorage productStorage = ProductMapper.mapToProductStorage(productSchema);

        return productService.addProductAsync(product, productStorage)
                .thenApply(v -> ResponseEntity.noContent().build());
    }

    @DeleteMapping(value = "/delete/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteProduct(@PathVariable int id) {
        return productService.deleteProductAsync(id)
                .thenApply(v -> ResponseEntity.noContent().build());
    }

    @GetMapping(value = "/get/{id}")
    public CompletableFuture<ResponseEntity<Product>> getProductById(@PathVariable int id) {
        return productService.getProductByIdAsync(id)
                .thenApply(product -> {
                    if (product != null) {
                        return ResponseEntity.ok(product);
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                });
    }

    @GetMapping(value = "/all")
    public CompletableFuture<ResponseEntity<List<Product>>> getAllProducts() {
        return productService.getAllProductsAsync()
                .thenApply(products -> ResponseEntity.ok(products));
    }
}
