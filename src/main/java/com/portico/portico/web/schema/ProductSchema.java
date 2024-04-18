package com.portico.portico.web.schema;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductSchema {
    private String name;
    private String manufacturer;
    private BigDecimal cost;
    private String dimensions;
    private BigDecimal weight;
    private Integer warehouseId;
    private Integer stock;
}
