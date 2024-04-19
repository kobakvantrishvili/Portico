package com.portico.portico.domain;

import lombok.Data;

@Data
public class OrderContent {
    private Integer productId;
    private Integer orderId;
    private Integer warehouseId;
    private Integer quantity;
}
