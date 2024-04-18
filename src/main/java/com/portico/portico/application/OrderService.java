package com.portico.portico.application;

import com.portico.portico.domain.Order;
import com.portico.portico.domain.OrderContents;
import com.portico.portico.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public CompletableFuture<Void> addOrderAsync(Order order) {
        return orderRepository.addOrderAsync(order);
    }

    public CompletableFuture<Void> deleteOrderAsync(int orderId) {
        return orderRepository.deleteOrderAsync(orderId);
    }

    public CompletableFuture<Order> getOrderByIdAsync(int orderId) {
        return orderRepository.getOrderByIdAsync(orderId);
    }

    public CompletableFuture<List<Order>> getAllOrdersAsync() {
        return orderRepository.getAllOrdersAsync();
    }
}
