package com.portico.portico.application;

import com.portico.portico.domain.ProductStorage;
import com.portico.portico.domain.Warehouse;
import com.portico.portico.repository.ProductStorageRepository;
import com.portico.portico.repository.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final ProductStorageRepository productStorageRepository;

    public WarehouseService(WarehouseRepository warehouseRepository, ProductStorageRepository productStorageRepository) {
        this.warehouseRepository = warehouseRepository;
        this.productStorageRepository = productStorageRepository;
    }

    public CompletableFuture<Warehouse> getWarehouseAsync(Integer warehouseId) {
        return warehouseRepository.getWarehouseAsync(warehouseId);
    }

    public CompletableFuture<List<Warehouse>> getAllWarehousesAsync() {
        return warehouseRepository.getAllProductsAsync();
    }

    public CompletableFuture<Void> restockWarehouse(Integer productId, Integer warehouseId, Integer quantityReceived) {
        // First, check if the warehouse and product exist
        CompletableFuture<ProductStorage> productStorageFuture = productStorageRepository.getProductStorageAsync(productId, warehouseId);

        return productStorageFuture.thenComposeAsync(productStorage -> {
            if (productStorage != null) {
                return productStorageRepository.restockProductAsync(productId, warehouseId, quantityReceived);
            } else {
                throw new IllegalArgumentException("The requested product was not found.");
            }
                });
    }


}
