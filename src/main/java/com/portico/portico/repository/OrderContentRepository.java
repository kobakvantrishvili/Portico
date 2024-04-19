package com.portico.portico.repository;
import com.portico.portico.domain.OrderContent;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class OrderContentRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderContentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Async
    public CompletableFuture<Void> addOrderContentAsync(OrderContent orderContent) {
        return CompletableFuture.runAsync(() -> {
            String sql = "INSERT INTO Order_Contents (product_id, order_id, warehouse_id, quantity) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql, orderContent.getProductId(), orderContent.getOrderId(), orderContent.getWarehouseId(), orderContent.getQuantity());
        });
    }

    @Async
    public CompletableFuture<List<OrderContent>> getOrderContentsByOrderIdAsync(int orderId) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT * FROM Order_Contents WHERE order_id = ?";
            try {
                return jdbcTemplate.query(sql, orderContentRowMapper, orderId);
            } catch (EmptyResultDataAccessException e) {
                return Collections.emptyList();
            }
        });
    }

    /* HELPERS */
    // RowMapper for mapping result set to OrderContent object
    private final RowMapper<OrderContent> orderContentRowMapper = (rs, rowNum) -> {
        OrderContent orderContent = new OrderContent();
        orderContent.setProductId(rs.getInt("product_id"));
        orderContent.setOrderId(rs.getInt("order_id"));
        orderContent.setWarehouseId(rs.getInt("warehouse_id"));
        orderContent.setQuantity(rs.getInt("quantity"));
        return orderContent;
    };
}