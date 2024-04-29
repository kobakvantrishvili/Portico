package com.portico.portico.web.controller;

import com.portico.portico.application.WarehouseService;
import com.portico.portico.domain.Warehouse;
import com.portico.portico.web.schema.RestockSchema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("ALL")
@RestController
@RequestMapping("/warehouse")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("/get/{id}")
    public CompletableFuture<ResponseEntity<Warehouse>> getWarehouseById(@PathVariable int id) {
        return warehouseService.getWarehouseAsync(id)
                .thenApply(warehouse -> {
                    if (warehouse != null) {
                        return ResponseEntity.ok(warehouse);
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                });
    }

    @GetMapping("/all")
    public CompletableFuture<ResponseEntity<List<Warehouse>>> getAllWarehouses() {
        return warehouseService.getAllWarehousesAsync()
                .thenApply(ResponseEntity::ok);
    }

    @PutMapping("/restock")
    public CompletableFuture<ResponseEntity<Void>> restockWarehouse(@RequestBody RestockSchema request) {
        int productId = request.getProductId();
        int warehouseId = request.getWarehouseId();
        int quantityReceived = request.getQuantityReceived();

        return warehouseService.restockWarehouse(productId, warehouseId, quantityReceived)
                .thenApply(v -> ResponseEntity.ok().build());
    }

}
