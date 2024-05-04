package com.portico.portico.application;

import com.portico.portico.domain.Customer;
import com.portico.portico.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {

        this.customerRepository = customerRepository;
    }

    public CompletableFuture<List<Customer>> getAllCustomersAsync() {
        return customerRepository.getAllCustomersAsync();
    }

    public CompletableFuture<Customer> getCustomerByIdAsync(Integer id) {
        return customerRepository.getCustomerByIdAsync(id);
    }
}
