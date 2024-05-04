package com.portico.portico.repository;

import com.portico.portico.domain.Customer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class CustomerRepository {

    private final JdbcTemplate jdbcTemplate;

    public CustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public CompletableFuture<List<Customer>> getAllCustomersAsync() {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT * FROM customer";
            return jdbcTemplate.query(sql, customerRowMapper);
        });
    }

    public CompletableFuture<Customer> getCustomerByIdAsync(Integer id) {
        Assert.notNull(id, "Customer ID must not be null");
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT * FROM customer WHERE customer_id = ?";
            try {
                return jdbcTemplate.queryForObject(sql, customerRowMapper, id);
            } catch (EmptyResultDataAccessException e) {
                return null; // Return null if no customer found
            }
        });
    }

    // RowMapper to map ResultSet to Customer object
    private static final RowMapper<Customer> customerRowMapper = new RowMapper<Customer>() {
        @Override
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            Customer customer = new Customer();
            customer.setId(rs.getInt("customer_id"));
            customer.setName(rs.getString("name"));
            customer.setAddress(rs.getString("address"));
            customer.setPhone(rs.getString("phone"));
            customer.setType(rs.getString("customer_type"));
            return customer;
        }
    };
}
