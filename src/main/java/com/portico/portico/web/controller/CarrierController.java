package com.portico.portico.web.controller;

import com.portico.portico.application.CarrierService;
import com.portico.portico.domain.Carrier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/carrier")
public class CarrierController {
    private final CarrierService carrierService;

    public CarrierController(CarrierService carrierService) {
        this.carrierService = carrierService;
    }

    @GetMapping("/all")
    public CompletableFuture<ResponseEntity<List<Carrier>>> getAllCarriers() {
        return carrierService.getAllCarriersAsync()
                .thenApply(carriers -> ResponseEntity.ok(carriers));
    }

    @GetMapping("/get/{id}")
    public CompletableFuture<ResponseEntity<Carrier>> getCarrierById(@PathVariable int id) {
        return carrierService.getCarrierByIdAsync(id)
                .thenApply(carrier -> {
                    if (carrier != null) {
                        return ResponseEntity.ok(carrier);
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                });
    }
}
