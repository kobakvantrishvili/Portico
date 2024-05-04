package com.portico.portico.web.schema;

import com.portico.portico.domain.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderSchema {
    private Integer carrierId;
    private Integer customerId;
    private OrderStatus status;;
    private Date orderDate;
    private Date deliveryDate;
    private String deliveryAddress;

    private List<OrderContentsSchema> orderContents;
}
