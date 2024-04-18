package com.portico.portico.repository;

import com.portico.portico.domain.Order;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
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
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;
    private final Environment env;

    public OrderRepository(JdbcTemplate jdbcTemplate, Environment env) {

        this.jdbcTemplate = jdbcTemplate;
        this.env = env;
    }

    @Async
    public CompletableFuture<Void> addOrderAsync(Order order) {
        return CompletableFuture.runAsync(() -> {
            SimpleJdbcCall addNewOrderCall = new SimpleJdbcCall(jdbcTemplate)
                    .withSchemaName(env.getProperty("spring.datasource.username"))
                    .withProcedureName("CHECKOUT")
                    .declareParameters(
                            new SqlParameter("carrierId", Types.INTEGER),
                            new SqlParameter("customerId", Types.INTEGER),
                            new SqlParameter("orderDate", Types.DATE),
                            new SqlParameter("totalCost", Types.DECIMAL),
                            new SqlParameter("status", Types.VARCHAR),
                            new SqlParameter("deliveryDate", Types.DATE),
                            new SqlParameter("deliveryAddress", Types.VARCHAR)
                    );
            MapSqlParameterSource mapSqlParameterSource = getParamSource(order);

            // Execute procedure call
            addNewOrderCall.execute(mapSqlParameterSource);
        });
    }

    @Async
    public CompletableFuture<Void> deleteOrderAsync(Integer orderId) {
        return CompletableFuture.runAsync(() -> {
            SimpleJdbcCall addNewOrderCall = new SimpleJdbcCall(jdbcTemplate)
                    .withSchemaName(env.getProperty("spring.datasource.username"))
                    .withProcedureName("CANCEL_ORDER")
                    .declareParameters(
                            new SqlParameter("orderNum", Types.INTEGER)
                    );
            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
            mapSqlParameterSource.addValue("orderNum", orderId);

            addNewOrderCall.execute(mapSqlParameterSource);
        });
    }

    @Async
    public CompletableFuture<Order> getOrderByIdAsync(int orderId) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT * FROM orders WHERE order_id = ?";
            try {
                return jdbcTemplate.queryForObject(sql, orderRowMapper, orderId);
            } catch (EmptyResultDataAccessException e) {
                return null;
            }
        });
    }

    @Async
    public CompletableFuture<List<Order>> getAllOrdersAsync() {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT * FROM orders";
            return jdbcTemplate.query(sql, orderRowMapper);
        });
    }

    /* HELPER METHODS */
    // Define a RowMapper to map ResultSet to Order object
    private final RowMapper<Order> orderRowMapper = (rs, rowNum) -> {
        Order order = new Order();
        order.setId(rs.getInt("order_id"));
        order.setCarrierId(rs.getInt("carrier_id"));
        order.setCustomerId(rs.getInt("customer_id"));
        order.setTotalCost(rs.getBigDecimal("total_cost"));
        order.setStatus(rs.getString("status"));
        order.setOrderDate(rs.getDate("order_date"));
        order.setDeliveryDate(rs.getDate("delivery_date"));
        order.setDeliveryAddress(rs.getString("delivery_address"));
        return order;
    };

    private static MapSqlParameterSource getParamSource(Order order) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("carrierId", order.getCarrierId());
        mapSqlParameterSource.addValue("customerId", order.getCustomerId());
        mapSqlParameterSource.addValue("orderDate", order.getOrderDate());
        mapSqlParameterSource.addValue("totalCost", order.getTotalCost());
        mapSqlParameterSource.addValue("status", order.getStatus());
        mapSqlParameterSource.addValue("deliveryDate", order.getDeliveryDate());
        mapSqlParameterSource.addValue("deliveryAddress", order.getDeliveryAddress());
        return mapSqlParameterSource;
    }

}
