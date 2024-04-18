package com.portico.portico.application;

import com.portico.portico.domain.Product;
import com.portico.portico.domain.ProductStorage;
import com.portico.portico.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductService {
    final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public CompletableFuture<Void> addProductAsync(Product product, ProductStorage productStorage) {
        return productRepository.addProductAsync(product, productStorage);
    }

    public CompletableFuture<Void> deleteProductAsync(Integer productId) {
        return productRepository.deleteProductAsync(productId);
    }

    public CompletableFuture<Product> getProductByIdAsync(Integer id) {
        return productRepository.getProductByIdAsync(id);
    }

    public CompletableFuture<List<Product>> getAllProductsAsync() {
        return productRepository.getAllProductsAsync();
    }
}
