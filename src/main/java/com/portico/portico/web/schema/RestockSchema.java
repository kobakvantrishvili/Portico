package com.portico.portico.web.schema;

import lombok.Data;

@Data
public class RestockSchema {
    private int productId;
    private int warehouseId;
    private int quantityReceived;
}
