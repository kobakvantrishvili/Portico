package com.portico.portico.domain;

import lombok.Data;
import java.math.BigDecimal;

@Data // auto-generates getters and setters
public class Product {
    private Integer id;
    private String name;
    private String manufacturer;
    private BigDecimal cost;
    private String dimensions;
    private BigDecimal weight;
}
