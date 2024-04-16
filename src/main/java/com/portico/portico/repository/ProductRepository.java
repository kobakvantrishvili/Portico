package com.portico.portico.repository;

import com.portico.portico.domain.Product;
import com.portico.portico.domain.ProductStorage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class ProductRepository {

    // dependency injection
    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Async
    public CompletableFuture<Void> addProductAsync(Product product, ProductStorage productStorage) {
        return CompletableFuture.runAsync(() -> {
            // Create an object to call the procedure
            SimpleJdbcCall addNewProductCall = new SimpleJdbcCall(jdbcTemplate)
                    .withSchemaName("PorticoDB")
                    .withProcedureName("ADD_NEW_PRODUCT")
                    .declareParameters(
                            new SqlParameter("productName", Types.NVARCHAR),
                            new SqlParameter("productManufacturer", Types.NVARCHAR),
                            new SqlParameter("productCost", Types.DECIMAL),
                            new SqlParameter("productDimensions", Types.NVARCHAR),
                            new SqlParameter("productWeight", Types.DECIMAL),
                            new SqlParameter("productStock", Types.INTEGER),
                            new SqlParameter("warehouseId", Types.INTEGER)
                    );
            MapSqlParameterSource mapSqlParameterSource = getParamSource(product, productStorage);

            // Execute procedure call
            addNewProductCall.execute(mapSqlParameterSource);
        });
    }

    @Async
    public CompletableFuture<Void> deleteProductAsync(int productId) {
        // Return CompletableFuture to run async task
        return CompletableFuture.runAsync(() -> {
            // Define SQL query for deleting a product using productId parameter
            String sql = "DELETE FROM Products WHERE product_id = ?";

            // Execute the delete operation
            jdbcTemplate.update(sql, productId);
        });
    }

    @Async
    public CompletableFuture<Product> getProductByIdAsync(int id) {
        // Use supplyAsync to execute the database query asynchronously
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT * FROM products WHERE product_id = ?";
            return jdbcTemplate.queryForObject(sql, productRowMapper, id);
        });
    }

    @Async
    public CompletableFuture<List<Product>> getAllProductsAsync() {
        // Use supplyAsync to execute the database query asynchronously
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT * FROM products";
            return jdbcTemplate.query(sql, productRowMapper);
        });
    }


    /* HELPER METHODS */

    // RowMapper to map ResultSet to Product object
    private static final RowMapper<Product> productRowMapper = (rs, rowNum) -> {
        Product product = new Product();
        product.setId(rs.getInt("product_id"));
        product.setName(rs.getString("name"));
        product.setManufacturer(rs.getString("manufacturer"));
        product.setCost(rs.getBigDecimal("cost"));
        product.setDimensions(rs.getString("dimensions"));
        product.setWeight(rs.getBigDecimal("weight"));
        return product;
    };

    // populates parameters for procedure call
    private static MapSqlParameterSource getParamSource(Product product, ProductStorage productStorage) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("productName", product.getName());
        mapSqlParameterSource.addValue("productManufacturer", product.getManufacturer());
        mapSqlParameterSource.addValue("productCost", product.getCost());
        mapSqlParameterSource.addValue("productDimensions", product.getDimensions());
        mapSqlParameterSource.addValue("productWeight", product.getWeight());
        mapSqlParameterSource.addValue("productStock", productStorage.getStock());
        mapSqlParameterSource.addValue("warehouseId", productStorage.getWarehouseId());
        return mapSqlParameterSource;
    }


}