package com.portico.portico.web.schema;

import lombok.Data;

@Data
public class OrderContentsSchema {
    private Integer productId;
    private Integer warehouseId;
    private Integer quantity;
}
