package com.portico.portico.repository;

import com.portico.portico.domain.Carrier;
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
public class CarrierRepository {

    private final JdbcTemplate jdbcTemplate;

    public CarrierRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public CompletableFuture<List<Carrier>> getAllCarriersAsync() {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT * FROM carrier";
            return jdbcTemplate.query(sql, carrierRowMapper);
        });
    }

    public CompletableFuture<Carrier> getCarrierByIdAsync(Integer id) {
        Assert.notNull(id, "Carrier ID must not be null");
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT * FROM carrier WHERE carrier_id = ?";
            try {
                return jdbcTemplate.queryForObject(sql, carrierRowMapper, id);
            } catch (EmptyResultDataAccessException e) {
                return null; // Return null if no carrier found
            }
        });
    }

    // RowMapper to map ResultSet to Carrier object
    private static final RowMapper<Carrier> carrierRowMapper = new RowMapper<Carrier>() {
        @Override
        public Carrier mapRow(ResultSet rs, int rowNum) throws SQLException {
            Carrier carrier = new Carrier();
            carrier.setId(rs.getInt("carrier_id"));
            carrier.setOrganizationName(rs.getString("organization_name"));
            carrier.setOfficeAddress(rs.getString("office_address"));
            carrier.setContactName(rs.getString("contact_name"));
            carrier.setContactPhone(rs.getString("contact_phone"));
            return carrier;
        }
    };
}
