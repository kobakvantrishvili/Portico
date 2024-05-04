package com.portico.portico.web.controller;

import com.portico.portico.domain.Customer;
import com.portico.portico.application.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/get/{id}")
    public CompletableFuture<ResponseEntity<Customer>> getCustomerById(@PathVariable int id) {
        return customerService.getCustomerByIdAsync(id)
                .thenApply(customer -> {
                    if (customer != null) {
                        return ResponseEntity.ok(customer);
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                });
    }

    @GetMapping("/all")
    public CompletableFuture<ResponseEntity<List<Customer>>> getAllCustomers() {
        return customerService.getAllCustomersAsync()
                .thenApply(customers -> ResponseEntity.ok(customers));
    }
}
