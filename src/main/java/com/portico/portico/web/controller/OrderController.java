package com.portico.portico.web.controller;

import com.portico.portico.application.OrderService;
import com.portico.portico.domain.Order;
import com.portico.portico.mapper.OrderMapper;
import com.portico.portico.web.schema.OrderSchema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("ALL")
@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/add")
    public CompletableFuture<ResponseEntity<Void>> addOrder(@RequestBody OrderSchema orderSchema) {
        Order order = OrderMapper.mapToOrder(orderSchema);

        return orderService.addOrderAsync(order)
                .thenApply(v -> ResponseEntity.ok().build());
    }

    @DeleteMapping("/delete/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteOrder(@PathVariable int id) {
        return orderService.deleteOrderAsync(id)
                .thenApply(v -> ResponseEntity.noContent().build());
    }

    @GetMapping("/get/{id}")
    public CompletableFuture<ResponseEntity<Order>> getOrderById(@PathVariable int id) {
        return orderService.getOrderByIdAsync(id)
                .thenApply(order -> {
                    if (order != null) {
                        return ResponseEntity.ok(order);
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                });
    }

    @GetMapping("/all")
    public CompletableFuture<ResponseEntity<List<Order>>> getAllOrders() {
        return orderService.getAllOrdersAsync()
                .thenApply(orders -> ResponseEntity.ok(orders));
    }
}
