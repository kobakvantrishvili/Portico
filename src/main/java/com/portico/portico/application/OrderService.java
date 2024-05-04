package com.portico.portico.application;

import com.portico.portico.domain.Order;
import com.portico.portico.domain.OrderContent;
import com.portico.portico.domain.ProductStorage;
import com.portico.portico.domain.enums.OrderStatus;
import com.portico.portico.repository.OrderContentRepository;
import com.portico.portico.repository.OrderRepository;
import com.portico.portico.repository.ProductStorageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderContentRepository orderContentsRepository;
    private final ProductStorageRepository productStorageRepository;

    public OrderService(OrderRepository orderRepository, OrderContentRepository orderContentsRepository,
                        ProductStorageRepository productStorageRepository) {
        this.orderRepository = orderRepository;
        this.orderContentsRepository = orderContentsRepository;
        this.productStorageRepository = productStorageRepository;
    }


    @Transactional
    public CompletableFuture<Integer> addOrderAsync(Order order) {
        return orderRepository.addOrderAsync(order)
                .thenComposeAsync(v -> {
                    // For each OrderContent, add it asynchronously and collect the futures in a list
                    List<CompletableFuture<Void>> futures = order.getOrderContents().stream()
                            .map(orderContent -> {
                                orderContent.setOrderId(order.getId());

                                CompletableFuture<Void> productStorageFuture = productStorageRepository
                                        .getProductStorageAsync(orderContent.getProductId(), orderContent.getWarehouseId())
                                        .thenAccept(pStorage -> {
                                            ProductStorage productStorage = new ProductStorage();
                                            productStorage.setProductId(pStorage.getProductId());
                                            productStorage.setWarehouseId(pStorage.getWarehouseId());
                                            productStorage.setStock(pStorage.getStock() - orderContent.getQuantity());

                                            productStorageRepository.updateProductStorageAsync(productStorage);
                                        });
                                CompletableFuture<Void> orderContentFuture = orderContentsRepository.addOrderContentAsync(orderContent);
                                CompletableFuture<Void> orderFuture = orderRepository.updateOrderCostAsync(order.getId());
                                return CompletableFuture.allOf(productStorageFuture, orderContentFuture, orderFuture);
                            })
                            .toList();

                    return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                            .thenApply(z -> order.getId());
                });
    }


    public CompletableFuture<Order> getOrderByIdAsync(int orderId) {
        CompletableFuture<Order> orderFuture = orderRepository.getOrderByIdAsync(orderId);
        CompletableFuture<List<OrderContent>> orderContentsFuture = orderContentsRepository.getOrderContentsByOrderIdAsync(orderId);

        return orderFuture.thenCombine(orderContentsFuture, (order, orderContents) -> {
            try {
                order.setOrderContents(orderContents);
            }
            catch (NullPointerException e){
                return null;
            }
            return order;
        });
    }

    public CompletableFuture<List<Order>> getAllOrdersAsync() {
        return orderRepository.getAllOrdersAsync();
    }

    @Transactional
    public CompletableFuture<Void> updateOrderStatusAsync(int orderId, OrderStatus status) {
        try {
            CompletableFuture<Order> orderFuture = orderRepository.getOrderByIdAsync(orderId).thenApply(order -> {
                if (order == null) {
                    throw new NullPointerException("Order not found with ID: " + orderId);
                }
                if (order.getStatus() == OrderStatus.CANCELLED) {
                    throw new IllegalArgumentException("Cannot change status for a cancelled order.");
                }
                if (status.getValue() < order.getStatus().getValue()) {
                    throw new IllegalArgumentException("New status has lower numeral value than the existing one.");
                }
                return order;
            });
            if(status == OrderStatus.CANCELLED){
                return orderRepository.deleteOrderAsync(orderId);
            }
            else{
                return orderFuture.thenComposeAsync(order -> orderRepository.updateOrderStatusAsync(orderId, status));
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            CompletableFuture<Void> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(e);
            return failedFuture;
        }
    }
}
