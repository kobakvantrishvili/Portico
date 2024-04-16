package com.portico.portico.domain;

import lombok.Data;

@Data
public class ProductStorage {
    private Integer warehouseId;
    private Integer productId;
    private Integer stock;
}
