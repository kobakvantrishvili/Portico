package com.portico.portico.repository;

import com.portico.portico.domain.Warehouse;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class WarehouseRepository {

    private final JdbcTemplate jdbcTemplate;

    public WarehouseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public CompletableFuture<Warehouse> getWarehouseAsync(Integer warehouseId){
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT * FROM Warehouse WHERE warehouse_id = ?";
            try {
                return jdbcTemplate.queryForObject(sql, warehouseRowMapper, warehouseId);
            } catch (EmptyResultDataAccessException e) {
                return null;
            }
        });
    }

    @Async
    public CompletableFuture<List<Warehouse>> getAllProductsAsync() {
        // Use supplyAsync to execute the database query asynchronously
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT * FROM Warehouse";
            return jdbcTemplate.query(sql, warehouseRowMapper);
        });
    }

    /* HELPERS */
    // Define a RowMapper to map the ResultSet to a Warehouse object
    private final RowMapper<Warehouse> warehouseRowMapper = (rs, rowNum) -> {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(rs.getInt("warehouse_id"));
        warehouse.setName(rs.getString("warehouse_name"));
        warehouse.setAddress(rs.getString("address"));
        warehouse.setContactName(rs.getString("contact_name"));
        warehouse.setContactPhone(rs.getString("contact_phone"));
        return warehouse;
    };
}
