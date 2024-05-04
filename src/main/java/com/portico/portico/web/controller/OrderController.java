package com.portico.portico.web.controller;

import com.portico.portico.application.OrderService;
import com.portico.portico.domain.Order;
import com.portico.portico.domain.enums.OrderStatus;
import com.portico.portico.mapper.OrderMapper;
import com.portico.portico.web.schema.OrderSchema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
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
    public CompletableFuture<ResponseEntity<Integer>> addOrder(@RequestBody OrderSchema orderSchema) {
        Order order = OrderMapper.mapToOrder(orderSchema);

        return orderService.addOrderAsync(order)
                .thenApply(v -> ResponseEntity.ok(v));
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

    @PutMapping("/update-status/{id}")
    public CompletableFuture<ResponseEntity<Object>> updateOrderStatus(
            @PathVariable int id,
            @RequestParam OrderStatus status) {
        return orderService.updateOrderStatusAsync(id, status)
                .thenApply(v -> ResponseEntity.ok().build())
                .exceptionally(ex -> {
                    if (ex.getCause() instanceof NullPointerException) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("Order with ID: " + id + " not found.");
                    }
                    if (ex.getCause() instanceof IllegalArgumentException) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Order status can't be updated to " + status + " : " + ex.getCause().getMessage());
                    } else {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("An unexpected error occurred.");
                    }
                });
    }
}
