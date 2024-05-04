package com.portico.portico.application;

import com.portico.portico.domain.Carrier;
import com.portico.portico.repository.CarrierRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CarrierService {

    private final CarrierRepository carrierRepository;

    public CarrierService(CarrierRepository carrierRepository) {
        this.carrierRepository = carrierRepository;
    }

    public CompletableFuture<List<Carrier>> getAllCarriersAsync() {
        return carrierRepository.getAllCarriersAsync();
    }

    public CompletableFuture<Carrier> getCarrierByIdAsync(Integer id) {
        return carrierRepository.getCarrierByIdAsync(id);
    }
}
