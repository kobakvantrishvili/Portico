package com.portico.portico.domain;

import lombok.Data;

@Data

public class OrderContents {
    private Integer productId;
    private Integer orderId;
    private Integer quantity;
}
