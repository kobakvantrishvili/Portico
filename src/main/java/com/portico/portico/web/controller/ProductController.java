package com.portico.portico.web.controller;

import com.portico.portico.application.ProductService;
import com.portico.portico.domain.Product;
import com.portico.portico.domain.ProductStorage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("ALL")
@Controller
@RequestMapping("product")
public class ProductController {
    final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @SuppressWarnings("Convert2MethodRef")
    @GetMapping(value = "/get/{id}")
    public CompletableFuture<ResponseEntity<Product>> getProductById(@PathVariable int id) {
        return productService.getProductByIdAsync(id)
                .thenApply(product -> ResponseEntity.ok(product));
    }

    @GetMapping(value = "/all")
    public CompletableFuture<ResponseEntity<List<Product>>> getAllProducts() {
        return productService.getAllProductsAsync()
                .thenApply(products -> ResponseEntity.ok(products));
    }

    @PostMapping(value = "/add")
    public CompletableFuture<ResponseEntity<Void>> addProduct(@RequestBody Product product, @RequestBody ProductStorage productStorage) {
        return productService.addProductAsync(product, productStorage)
                .thenApply(v -> ResponseEntity.noContent().build());
    }

    @DeleteMapping(value = "/delete/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteProduct(@PathVariable int id) {
        return productService.deleteProductAsync(id)
                .thenApply(v -> ResponseEntity.noContent().build());
    }

}
