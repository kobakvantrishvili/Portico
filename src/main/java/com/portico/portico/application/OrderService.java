package com.portico.portico.application;

import com.portico.portico.domain.Order;
import com.portico.portico.domain.OrderContent;
import com.portico.portico.domain.ProductStorage;
import com.portico.portico.repository.OrderContentRepository;
import com.portico.portico.repository.OrderRepository;
import com.portico.portico.repository.ProductStorageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
    public CompletableFuture<Void> addOrderAsync(Order order) {
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
                                return CompletableFuture.allOf(productStorageFuture, orderContentFuture);
                            })
                            .collect(Collectors.toList());

                    return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
                });
    }

    public CompletableFuture<Void> deleteOrderAsync(int orderId) {
        return orderRepository.deleteOrderAsync(orderId);
    }

    public CompletableFuture<Order> getOrderByIdAsync(int orderId) {
        CompletableFuture<Order> orderFuture = orderRepository.getOrderByIdAsync(orderId);
        CompletableFuture<List<OrderContent>> orderContentsFuture = orderContentsRepository.getOrderContentsByOrderIdAsync(orderId);

        return orderFuture.thenCombine(orderContentsFuture, (order, orderContents) -> {
            order.setOrderContents(orderContents);
            return order;
        });
    }

    public CompletableFuture<List<Order>> getAllOrdersAsync() {
        return orderRepository.getAllOrdersAsync();
    }
}
