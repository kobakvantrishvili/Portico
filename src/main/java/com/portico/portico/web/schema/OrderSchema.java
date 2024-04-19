package com.portico.portico.web.schema;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderSchema {
    private Integer carrierId;
    private Integer customerId;
    private String status;
    private Date orderDate;
    private Date deliveryDate;
    private String deliveryAddress;

    private List<OrderContentsSchema> orderContents;
}
