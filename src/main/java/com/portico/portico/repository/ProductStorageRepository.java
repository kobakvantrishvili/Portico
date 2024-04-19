package com.portico.portico.repository;

import com.portico.portico.domain.ProductStorage;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletableFuture;

@Repository
public class ProductStorageRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductStorageRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Async
    public CompletableFuture<Void> updateProductStorageAsync(ProductStorage productStorage) {
        return CompletableFuture.runAsync(() -> {
            // Check if the stock value is greater than or equal to 0
            if (productStorage.getStock() >= 0) {
                // Update the stock in Product_Storage based on the productId and warehouseId
                String sql = "UPDATE Product_Storage SET stock = ? WHERE product_id = ? AND warehouse_id = ?";
                jdbcTemplate.update(sql, productStorage.getStock(), productStorage.getProductId(), productStorage.getWarehouseId());
            }
            else{
                throw new IllegalArgumentException("Stock must be greater than or equal to 0.");
            }
        });
    }


    @Async
    public CompletableFuture<ProductStorage> getProductStorageAsync(int productId, int warehouseId) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT * FROM Product_Storage WHERE product_id = ? AND warehouse_id = ?";
            try {
                return jdbcTemplate.queryForObject(sql, productStorageRowMapper, productId, warehouseId);
            } catch (EmptyResultDataAccessException e) {
                return null;
            }
        });
    }

    /* HELPERS */
    // Define a RowMapper to map the ResultSet to a ProductStorage object
    private final RowMapper<ProductStorage> productStorageRowMapper = (rs, rowNum) -> {
        ProductStorage productStorage = new ProductStorage();
        productStorage.setWarehouseId(rs.getInt("warehouse_id"));
        productStorage.setProductId(rs.getInt("product_id"));
        productStorage.setStock(rs.getInt("stock"));
        return productStorage;
    };
}
